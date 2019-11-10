package gov.ismonnet.netty.codecs;

import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.exceptions.NetworkException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

/**
 * Encodes packets to {@link ByteBuf}s
 *
 * @author Ferlo
 */
@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    // Constants

    private static final Logger LOGGER = LogManager.getLogger(PacketEncoder.class);

    // Attributes

    /**
     * Function returning the id for the given packet class
     */
    private final Function<Class<? extends Packet>, Byte> packetToId;

    /**
     * Constructs a packet encoder
     *
     * @param packetToId function returning the id for the given packet class
     */
    public PacketEncoder(Function<Class<? extends Packet>, Byte> packetToId) {
        this.packetToId = packetToId;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          Packet msg,
                          ByteBuf out) throws Exception {

        LOGGER.trace("Sending packet {}", msg);
        encode(msg, out);
    }

    /**
     * Encodes the given packet in the buffer
     *
     * @param msg packet to encode
     * @param out buffer to write into
     */
    public void encode(Packet msg,
                       ByteBuf out) {

        try {
            out.writeByte(packetToId.apply(msg.getClass()));
            msg.writePacket(out);
        } catch (Exception ex) {
            throw new NetworkException("Couldn't encode packet", ex);
        }
    }
}
