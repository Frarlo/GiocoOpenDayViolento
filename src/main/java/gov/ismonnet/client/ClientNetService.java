package gov.ismonnet.client;

import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.codecs.ByteStuffingDecoder;
import gov.ismonnet.netty.codecs.ByteStuffingEncoder;
import gov.ismonnet.netty.codecs.PacketDecoder;
import gov.ismonnet.netty.codecs.PacketEncoder;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketIdService;
import gov.ismonnet.netty.packets.DisconnectPacket;
import gov.ismonnet.netty.packets.PingPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ClientNetService implements NetService, LifeCycle {

    private final static int SHUTDOWN_TIMEOUT = 5000;

    private final Bootstrap bootstrap;
    private final InetSocketAddress address;

    private final int keepAliveTimeout;

    private volatile boolean isConnected;
    private EventLoopGroup group;
    private ChannelFuture channelFuture;

    private Future<?> pingFuture;

    @Inject ClientNetService(@Named("socket_address") InetSocketAddress address,
                             @Named("keep_alive_timeout") int keepAliveTimeout,
                             PacketIdService packetIdService,
                             LifeCycleService lifeCycleManager) {
        this.address = address;
        this.keepAliveTimeout = keepAliveTimeout;

        this.bootstrap = new Bootstrap()
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // Decoders

                        ch.pipeline().addLast("timeout", new ReadTimeoutHandler(keepAliveTimeout, TimeUnit.MILLISECONDS));
                        ch.pipeline().addLast("framer", new ByteStuffingDecoder());
                        ch.pipeline().addLast("decoder", new PacketDecoder(packetIdService::getParserById));

                        ch.pipeline().addLast("ping_handler", new KeepAliveHandler());
                        ch.pipeline().addLast("packet_handler", new PacketHandler());

                        // Encoders

                        ch.pipeline().addLast("frame_encoder", new ByteStuffingEncoder());
                        ch.pipeline().addLast("encoder", new PacketEncoder(packetIdService::getPacketId));
                    }
                })
                .remoteAddress(address);

        this.isConnected = false;
        lifeCycleManager.register(this);
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
        if(pingFuture != null)
            pingFuture.cancel(true);
        if(isConnected)
            sendPacket(new DisconnectPacket()).get();
        group.shutdownGracefully().await(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Future<Void> sendPacket(Packet packet) {
        return channelFuture.channel().writeAndFlush(packet);
    }

    private final class KeepAliveHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            final int delay = keepAliveTimeout / 2;
            pingFuture = group.scheduleAtFixedRate(() -> sendPacket(new PingPacket()), delay, delay, TimeUnit.MILLISECONDS);

            super.channelActive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

//            if(cause instanceof DelimiterDecoderException) {
//                LOGGER.error("Exception while framing packets", cause);
//
//            } else if(cause instanceof NetworkException) {
//                LOGGER.error("Exception while decoding packets", cause);
//
//            } else  {
//                if(cause instanceof ReadTimeoutException)
//                    LOGGER.error("Connection timed out", cause);
//                else
//                    LOGGER.error("Uncaught exception inside the Netty pipeline (Ctx: {})", ctx, cause);
//                closeChannel();
//            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//            LOGGER.trace("Channel inactive");
//            super.channelInactive(ctx);
        }
    }

    private final class PacketHandler extends SimpleChannelInboundHandler<Packet> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {

//            if(msg instanceof KickPacket)
//                closeChannel();

            // Todo: handle the packets somehow
//            LOGGER.info("Handle packet {}", msg);
        }
    }
}
