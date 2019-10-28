package gov.ismonnet.netty.core;

/**
 * Packet parser for classes which only parse packets coming from the client
 *
 * @author Ferlo
 *
 * @see CPacket
 * @see PacketParser
 */
public interface CPacketParser extends PacketParser<CPacket> {
}
