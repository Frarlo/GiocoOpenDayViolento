package gov.ismonnet.netty.client;

import gov.ismonnet.event.EventListener;
import gov.ismonnet.event.bus.BaseBus;
import gov.ismonnet.event.bus.WeakBus;
import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.codecs.ByteStuffingDecoder;
import gov.ismonnet.netty.codecs.ByteStuffingEncoder;
import gov.ismonnet.netty.codecs.PacketDecoder;
import gov.ismonnet.netty.codecs.PacketEncoder;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketIdService;
import gov.ismonnet.netty.exceptions.DelimiterDecoderException;
import gov.ismonnet.netty.exceptions.NetworkException;
import gov.ismonnet.netty.packets.DisconnectPacket;
import gov.ismonnet.netty.packets.KickPacket;
import gov.ismonnet.netty.packets.PingPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ClientNetService implements NetService, LifeCycle {

    private static final Logger LOGGER = LogManager.getLogger(ClientNetService.class);
    private final static int SHUTDOWN_TIMEOUT = 5000;

    private final LifeCycleService lifeCycleService;

    private final Bootstrap bootstrap;
    private final InetSocketAddress address;

    private final BaseBus<Packet> delegateBus;

    private final int keepAliveTimeout;

    private volatile boolean isConnected;
    private volatile boolean isStopped;

    private EventLoopGroup group;
    private ChannelFuture channelFuture;

    private Future<?> pingFuture;

    @Inject ClientNetService(@Named("socket_address") InetSocketAddress address,
                             @Named("keep_alive_timeout") int keepAliveTimeout,
                             PacketIdService packetIdService,
                             LifeCycleService lifeCycleService) {
        this.address = address;
        this.keepAliveTimeout = keepAliveTimeout;
        this.lifeCycleService = lifeCycleService;

        this.delegateBus = new WeakBus<>();
        this.bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // Decoders

                        ch.pipeline().addLast("timeout", new ReadTimeoutHandler(keepAliveTimeout, TimeUnit.MILLISECONDS));
                        ch.pipeline().addLast("framer", new ByteStuffingDecoder());
                        ch.pipeline().addLast("decoder", new PacketDecoder(packetIdService::getParserById));

                        // Encoders

                        ch.pipeline().addLast("frame_encoder", new ByteStuffingEncoder());
                        ch.pipeline().addLast("encoder", new PacketEncoder(packetIdService::getPacketId));

                        // Handlers

                        ch.pipeline().addLast("ping_handler", new KeepAliveHandler());
                        ch.pipeline().addLast("packet_handler", new PacketHandler());
                    }
                })
                .remoteAddress(address);

        this.isConnected = false;
        lifeCycleService.register(this);
    }

    @Override
    public void start() throws Exception {
        group = new NioEventLoopGroup();
        channelFuture = bootstrap.group(group)
                .connect()
                .sync();
        isConnected = true;
    }

    @Override
    public void stop() throws Exception {
        isStopped = true;

        if(pingFuture != null)
            pingFuture.cancel(true);
        if(isConnected && channelFuture.channel().isOpen())
            sendPacket(new DisconnectPacket()).get();
        group.shutdownGracefully().await(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Future<Void> sendPacket(Packet packet) {
        LOGGER.trace("Sending packet {}", packet);
        return channelFuture.channel().writeAndFlush(packet);
    }

    private final class KeepAliveHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            final int delay = keepAliveTimeout / 2;
            pingFuture = group.scheduleAtFixedRate(() -> {
                if(channelFuture.channel().isOpen())
                    sendPacket(new PingPacket());
            }, delay, delay, TimeUnit.MILLISECONDS);

            super.channelActive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

            if(cause instanceof DelimiterDecoderException) {
                LOGGER.error("Exception while framing packets", cause);

            } else if(cause instanceof NetworkException) {
                LOGGER.error("Exception while decoding packets", cause);

            } else  {
                if(cause instanceof ReadTimeoutException)
                    LOGGER.error("Connection timed out", cause);
                else
                    LOGGER.error("Uncaught exception inside the Netty pipeline (Ctx: {})", ctx, cause);
                ctx.close();
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            LOGGER.trace("Channel inactive");
            super.channelInactive(ctx);

            if(!isStopped)
                lifeCycleService.stop();
        }
    }

    private final class PacketHandler extends SimpleChannelInboundHandler<Packet> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {

            if(msg instanceof KickPacket)
                ctx.close();

            LOGGER.trace("Handle packet {}", msg);
            post(msg);
        }
    }

    // Delegate event bus

    @Override
    public void register(EventListener<? extends Packet> listener) {
        delegateBus.register(listener);
    }

    @Override
    public void registerObj(Object obj) {
        delegateBus.registerObj(obj);
    }

    @Override
    public void registerObj(Object obj, Class<? extends Packet>... events) {
        delegateBus.registerObj(obj, events);
    }

    @Override
    public void unregister(EventListener<? extends Packet> listener) {
        delegateBus.unregister(listener);
    }

    @Override
    public void unregisterObj(Object obj) {
        delegateBus.unregisterObj(obj);
    }

    @Override
    public void unregisterObj(Object obj, Class<? extends Packet>... events) {
        delegateBus.unregisterObj(obj, events);
    }

    @Override
    public Packet post(Packet event) {
        return delegateBus.post(event);
    }
}
