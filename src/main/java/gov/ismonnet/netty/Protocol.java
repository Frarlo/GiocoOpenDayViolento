package gov.ismonnet.netty;

import gov.ismonnet.netty.codecs.ByteStuffingDecoder;
import gov.ismonnet.netty.codecs.ByteStuffingEncoder;
import gov.ismonnet.netty.codecs.PacketDecoder;
import gov.ismonnet.netty.codecs.PacketEncoder;
import gov.ismonnet.netty.core.*;
import gov.ismonnet.netty.packets.DisconnectPacket;
import gov.ismonnet.netty.packets.KickPacket;
import gov.ismonnet.netty.packets.PingPacket;
import gov.ismonnet.netty.packets.PongPacket;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Class handling the netty protocol
 *
 * @author Ferlo
 */
public class Protocol {

    private Protocol() {} // Limit scope

    /**
     * Time in milliseconds before a {@link ReadTimeoutException}
     * should be raised in the netty pipeline
     */
    public static final int KEEPALIVE_TIMEOUT = 30000;

    /**
     * Map associating to each packet class its ID.
     *
     * I've put a Map here and not a method inside the packet themselves
     * just so I could easily see all of them in the same place.
     */
    private final static Map<Class<? extends Packet>, Byte> PACKET_IDS;

    /**
     * Map associating {@link SPacket} ids with their {@link SPacketParser}
     */
    private final static Map<Byte, SPacketParser> SERVER_PACKETS_PARSERS;
    /**
     * Map associating a {@link CPacket} id with it's {@link CPacketParser}
     */
    private final static Map<Byte, CPacketParser> CLIENT_PACKETS_PARSERS;

    // Populate the maps kthx

    static {
        final Map<Class<? extends Packet>, Byte> temp = new HashMap<>();

        // The packets with the same id are
        // always a Server-Client pair
        // So they can have the same id

        temp.put(PingPacket.class, (byte) 0);
        temp.put(PongPacket.class, (byte) 0);

        temp.put(DisconnectPacket.class, (byte) 1);
        temp.put(KickPacket.class, (byte) 1);

        PACKET_IDS = Collections.unmodifiableMap(temp);
    }

    static {
        final Map<Byte, SPacketParser> temp = new HashMap<>();

        temp.put(getPacketID(PongPacket.class), PongPacket.PARSER);
        temp.put(getPacketID(KickPacket.class), KickPacket.PARSER);

        SERVER_PACKETS_PARSERS = Collections.unmodifiableMap(temp);
    }

    static {
        final Map<Byte, CPacketParser> temp = new HashMap<>();

        temp.put(getPacketID(PingPacket.class), PingPacket.PARSER);
        temp.put(getPacketID(DisconnectPacket.class), DisconnectPacket.PARSER);

        CLIENT_PACKETS_PARSERS = Collections.unmodifiableMap(temp);
    }

    /**
     * Returns the id for the given packet class
     *
     * @param clazz Packet class
     * @return packet id
     */
    private static byte getPacketID(Class<? extends Packet> clazz) {
        return PACKET_IDS.get(clazz);
    }

    /**
     * Populates the given {@link ChannelPipeline} with the server codecs
     *
     * @param pipeline pipeline to populate
     * @return the given pipeline
     */
    public static ChannelPipeline populateClientPipeline(ChannelPipeline pipeline) {

        // Decoders

        pipeline.addLast("timeout", new ReadTimeoutHandler(KEEPALIVE_TIMEOUT, TimeUnit.MILLISECONDS));
        pipeline.addLast("framer", new ByteStuffingDecoder());
        pipeline.addLast("decoder", new PacketDecoder(SERVER_PACKETS_PARSERS::get));

        // Encoders

        pipeline.addLast("frame_encoder", new ByteStuffingEncoder());
        pipeline.addLast("encoder", new PacketEncoder(Protocol::getPacketID));

        return pipeline;
    }

    /**
     * Populates the given {@link ChannelPipeline} with the client codecs
     *
     * @param pipeline pipeline to populate
     * @return the given pipeline
     */
    public static ChannelPipeline populateServerPipeline(ChannelPipeline pipeline) {

        // Decoders

        pipeline.addLast("timeout", new ReadTimeoutHandler(KEEPALIVE_TIMEOUT, TimeUnit.MILLISECONDS));
        pipeline.addLast("framer", new ByteStuffingDecoder(8192));
        pipeline.addLast("decoder", new PacketDecoder(CLIENT_PACKETS_PARSERS::get));

        // Encoders

        pipeline.addLast("frame_encoder", new ByteStuffingEncoder());
        pipeline.addLast("encoder", new PacketEncoder(Protocol::getPacketID));

        return pipeline;
    }
}
