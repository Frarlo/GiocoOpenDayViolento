package gov.ismonnet.netty.packets;

import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketParser;
import io.netty.buffer.ByteBuf;

/**
 * Packet sent from the client to the server to indicate that it's disconnecting
 *
 * @author Ferlo
 */
public class DisconnectPacket implements Packet {

    @Override
    public void writePacket(ByteBuf buf) {
    }

    /**
     * Constructs a new {@link DisconnectPacket} from the given data buffer
     */
    public static final PacketParser PARSER = (msg) -> new DisconnectPacket();
}
