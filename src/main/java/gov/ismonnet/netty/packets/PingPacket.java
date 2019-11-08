package gov.ismonnet.netty.packets;

import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketParser;
import io.netty.buffer.ByteBuf;

public class PingPacket implements Packet {

    @Override
    public void writePacket(ByteBuf buf) {
    }

    /**
     * Generates a ping packet on the heap to be used.
     */
    public static final PacketParser PARSER = (msg) -> new PingPacket();
}
