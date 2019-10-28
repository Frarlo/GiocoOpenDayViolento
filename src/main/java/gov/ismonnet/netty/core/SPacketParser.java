package gov.ismonnet.netty.core;

/**
 * Packet parser for classes which only parse packets coming from the server
 *
 * @author Ferlo
 *
 * @see SPacket
 * @see PacketParser
 */
public interface SPacketParser extends PacketParser<SPacket> {
}
