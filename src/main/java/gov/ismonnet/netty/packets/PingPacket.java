package gov.ismonnet.netty.packets;

import gov.ismonnet.netty.core.BasePacket;
import gov.ismonnet.netty.core.CPacket;
import gov.ismonnet.netty.core.CPacketParser;
import io.netty.buffer.ByteBuf;

public class PingPacket extends BasePacket implements CPacket {

    @Override
    public void writePacket(ByteBuf buf) throws Exception {
    }

    /**
     * Generates a ping packet on the heap to be used.
     */
    public static final CPacketParser PARSER = (msg) -> new PingPacket();
}
