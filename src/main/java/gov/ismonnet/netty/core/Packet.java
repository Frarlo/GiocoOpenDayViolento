package gov.ismonnet.netty.core;

import io.netty.buffer.ByteBuf;

/**
 * Base interface for packets
 *
 * @author Ferlo
 */
public interface Packet {

    /**
     * Write this packet content to the buffer
     *
     * @param buf buffer to write into
     * @throws Exception if anything goes wrong
     */
    void writePacket(ByteBuf buf) throws Exception;

    /**
     * Returns the context of this packet
     *
     * @return packet context
     */
    PacketContext getContext();
}
