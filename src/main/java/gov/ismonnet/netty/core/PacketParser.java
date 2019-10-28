package gov.ismonnet.netty.core;

import io.netty.buffer.ByteBuf;

/**
 * Base interface for classes able to build a packet from
 * data contained in a buffer
 *
 * @param <T> Packet type
 * @author Ferlo
 */
public interface PacketParser<T extends Packet> {

    /**
     * Builds a packet from the given buffer
     *
     * @param buf buffer containing packet data
     * @return packet built from the buffer
     * @throws Exception if the buffer doesn't contain correctly encoded data
     */
    T parse(ByteBuf buf) throws Exception;
}
