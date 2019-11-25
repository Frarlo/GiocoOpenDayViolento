package gov.ismonnet.netty.packets;

import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketParser;
import io.netty.buffer.ByteBuf;

public class GoalPacket implements Packet {

    @Override
    public String toString() {
        return "GoalPacket{}";
    }

    @Override
    public void writePacket(ByteBuf buf) {
    }

    public static final PacketParser<GoalPacket> PARSER = buf -> new GoalPacket();
}
