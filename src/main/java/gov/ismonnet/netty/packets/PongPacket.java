package gov.ismonnet.netty.packets;

import gov.ismonnet.netty.core.BasePacket;
import gov.ismonnet.netty.core.SPacket;
import gov.ismonnet.netty.core.SPacketParser;
import io.netty.buffer.ByteBuf;

public class PongPacket extends BasePacket implements SPacket {

    @Override
    public void writePacket(ByteBuf buf) throws Exception {
    }

    /**
     * Parser that generates a pong packet on the heap to be used.
     */
    public static final SPacketParser PARSER = (msg) -> new PongPacket();
}
