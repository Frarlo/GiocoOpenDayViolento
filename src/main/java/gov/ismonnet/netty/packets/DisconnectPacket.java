package gov.ismonnet.netty.packets;

import gov.ismonnet.netty.core.BasePacket;
import gov.ismonnet.netty.core.CPacket;
import gov.ismonnet.netty.core.CPacketParser;
import io.netty.buffer.ByteBuf;

/**
 * Packet sent from the client to the server to indicate that it's disconnecting
 *
 * @author Ferlo
 */
public class DisconnectPacket extends BasePacket implements CPacket {

    @Override
    public void writePacket(ByteBuf buf) throws Exception {
    }

    /**
     * Constructs a new {@link DisconnectPacket} from the given data buffer
     */
    public static final CPacketParser PARSER = (msg) -> new DisconnectPacket();
}
