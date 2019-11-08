package gov.ismonnet.server;

import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.codecs.ByteStuffingDecoder;
import gov.ismonnet.netty.codecs.ByteStuffingEncoder;
import gov.ismonnet.netty.codecs.PacketDecoder;
import gov.ismonnet.netty.codecs.PacketEncoder;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketIdService;
import gov.ismonnet.netty.packets.KickPacket;
import gov.ismonnet.netty.packets.PingPacket;
import gov.ismonnet.netty.packets.PongPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ServerNetService implements NetService, LifeCycle {

    private final static int SHUTDOWN_TIMEOUT = 5000;

    private final ServerBootstrap bootstrap;
    private final int port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ChannelFuture bindFuture;

    private CompletableFuture<Channel> clientFuture;
    private Channel clientChannel;

    @Inject ServerNetService(@Named("socket_port") int port,
                             @Named("keep_alive_timeout") int keepAliveTimeout,
                             PacketIdService packetIdService,
                             LifeCycleService lifeCycleService) {
        this.port = port;
        this.bootstrap = new ServerBootstrap()
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // Decoders

                        ch.pipeline().addLast("timeout", new ReadTimeoutHandler(keepAliveTimeout, TimeUnit.MILLISECONDS));
                        ch.pipeline().addLast("framer", new ByteStuffingDecoder());
                        ch.pipeline().addLast("decoder", new PacketDecoder(packetIdService::getParserById));

                        ch.pipeline().addLast("keep_alive_handler", new KeepAliveHandler());
                        ch.pipeline().addLast("packet_handler", new PacketHandler());

                        // Encoders

                        ch.pipeline().addLast("frame_encoder", new ByteStuffingEncoder());
                        ch.pipeline().addLast("encoder", new PacketEncoder(packetIdService::getPacketId));
                    }
                })
                .localAddress(port);

        lifeCycleService.register(this);
    }

    @Override
    public void start() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        clientFuture = new CompletableFuture<>();
        bindFuture = bootstrap
                .group(bossGroup, workerGroup)
                .bind()
                .sync();

        clientChannel = clientFuture.get();
    }

    @Override
    public void stop() throws Exception {
        bossGroup.shutdownGracefully().await(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS);
        workerGroup.shutdownGracefully().await(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public Future<Void> sendPacket(Packet packet) {
        return clientChannel.writeAndFlush(packet);
    }

    private final class KeepAliveHandler extends SimpleChannelInboundHandler<Packet> {

        @Override
        public void channelRead(ChannelHandlerContext ctx,
                                Object msg) throws Exception {
            super.channelRead(ctx, msg);
            ctx.fireChannelRead(msg); // Make the packet go through the pipeline
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Packet msg) {
            if(msg instanceof PingPacket)
                ctx.write(new PongPacket());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//            if(cause instanceof DelimiterDecoderException) {
//                LOGGER.error("Exception while framing packets (Sender: {})", ctx, cause);
//
//            } else if(cause instanceof NetworkException) {
//                LOGGER.error("Exception while decoding packets (Sender: {})", ctx, cause);
//
//            } else  {
//                if(cause instanceof ReadTimeoutException)
//                    LOGGER.error("Connection timed out (Ctx: {})", ctx, cause);
//                else
//                    LOGGER.error("Uncaught exception inside the Netty pipeline (Ctx: {}) {}", ctx, cause);
//                ctx.close();
//            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            if(clientChannel != null) {
                ctx.writeAndFlush(new KickPacket("A player is already connected")).sync();
                ctx.close();
            }

            clientFuture.complete(ctx.channel());
            super.channelActive(ctx);
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

//            if(msg instanceof DisconnectPacket)
//                closeChannel();

            // Todo: handle the packets somehow
//            LOGGER.info("Handle packet {}", msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
            super.channelReadComplete(ctx);
        }
    }
}
