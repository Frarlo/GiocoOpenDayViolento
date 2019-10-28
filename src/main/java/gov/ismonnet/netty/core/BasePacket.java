package gov.ismonnet.netty.core;

/**
 * Base class that all packets should extend.
 * Each packet also needs to implement either {@link CPacket} or {@link SPacket},
 * depending on who the sender is.
 *
 * @author Ferlo
 */
public abstract class BasePacket implements Packet {

    private PacketContext ctx;

    @Override
    public PacketContext  getContext() {
        return ctx;
    }

    /**
     * Set the current packet context. Can only be set once.
     *
     * @param ctx packet context
     */
    public void setContext(PacketContext ctx) {
        if(this.ctx != null)
            throw new AssertionError("Packet context has already been set");
        this.ctx = ctx;
    }
}
