package gov.ismonnet.netty.core;

public interface PacketIdService {

    byte getPacketId(Class<? extends Packet> clazz);

    PacketParser getParserById(byte id);
}
