package gov.ismonnet.netty.packets;

import gov.ismonnet.netty.ByteBufHelper;
import gov.ismonnet.netty.core.BasePacket;
import gov.ismonnet.netty.core.SPacket;
import gov.ismonnet.netty.core.SPacketParser;
import io.netty.buffer.ByteBuf;

/**
 * Packet sent from the server to the client
 * to communicate that it's being forcefully disconnected (?)
 *
 * @author Ferlo
 */
public class KickPacket extends BasePacket implements SPacket {

    /**
     * Kick reason (or an empty string if not specified)
     */
    private final String reason;

    /**
     * Constructs a new Kick packet without specifying a reason
     */
    public KickPacket() {
        this("");
    }

    /**
     * Constructs a new Kick packet with the given reason
     *
     * @param reason kick reason
     * @throws RuntimeException if the reason is null
     */
    public KickPacket(String reason) {

        if(reason == null) // It's easier to just send an empty string
            throw new RuntimeException("Reason string cannot be null.");

        this.reason = reason;
    }

    /**
     * Returns the kick reason (or an empty string if not specified)
     *
     * @return kick reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns true if the packet specified a kick reason
     *
     * @return true if there's a reason
     */
    public boolean hasReason() {
        return !reason.equals("");
    }

    @Override
    public void writePacket(ByteBuf buf) throws Exception {
        ByteBufHelper.writeString(buf, reason);
    }

    @Override
    public String toString() {
        return "KickPacket{" +
                "reason='" + reason + '\'' +
                '}';
    }

    /**
     * Constructs a new {@link KickPacket} from the given data buffer
     */
    public static final SPacketParser PARSER = (msg) -> new KickPacket(ByteBufHelper.readString(msg));
}
