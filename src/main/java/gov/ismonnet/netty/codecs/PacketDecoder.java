package gov.ismonnet.netty.codecs;

import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketParser;
import gov.ismonnet.netty.exceptions.NetworkException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Function;

/**
 * Decodes {@link ByteBuf}s frames into {@link Packet}
 *
 * @author Ferlo
 */
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    // Constants

    /**
     * Logger
     */
    private static final Logger LOGGER = LogManager.getLogger(PacketDecoder.class);

    // Attributes

    /**
     * Function returning the parser for the given packet id
     */
    private final Function<Byte, PacketParser> idToParser;

    /**
     * Constructs a packet decoder
     *
     * @param idToParser function returning the parser for the given packet id
     */
    public PacketDecoder(Function<Byte, PacketParser> idToParser) {
        this.idToParser = idToParser;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf msg,
                          List<Object> out) throws Exception {

        final Packet packet = decode(msg);

        LOGGER.trace("Received packet {}", packet);
        out.add(packet);
    }

    /**
     * Decodes the given frame
     *
     * @param frame buffer containing the packet data
     * @return decoded packet
     * @throws NetworkException if the packet couldn't be parsed
     */
    public Packet decode(ByteBuf frame) throws NetworkException {

        final byte packetId = frame.readByte();
        final PacketParser packetParser = idToParser.apply(packetId);

        if(packetParser == null)
            throw new NetworkException("There is no parser for the given ID (" + packetId + ')');

        try {
            return packetParser.parse(frame);
        } catch(Exception e) {
            throw new NetworkException("Couldn't parse packet", e);
        }
    }
}
