package gov.ismonnet.netty.packets;

import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketParser;
import io.netty.buffer.ByteBuf;

public class PongPacket implements Packet {

    @Override
    public void writePacket(ByteBuf buf) {
    }

    /**
     * Parser that generates a pong packet on the heap to be used.
     */
    public static final PacketParser PARSER = (msg) -> new PongPacket();
}
