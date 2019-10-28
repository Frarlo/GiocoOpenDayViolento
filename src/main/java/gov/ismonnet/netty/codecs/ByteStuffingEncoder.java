package gov.ismonnet.netty.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Delimits each frame with the specified bytes
 *
 * @author Ferlo
 *
 * See <a href="https://en.wikipedia.org/wiki/High-Level_Data_Link_Control#Asynchronous_framing">HDLC framing</a>
 * See <a href="https://en.wikipedia.org/wiki/Consistent_Overhead_Byte_Stuffing">Byte Stuffing</a>
 */
public class ByteStuffingEncoder extends MessageToByteEncoder<ByteBuf> {

    // Constants

    /**
     * Data Link Escape. Used as the default byte for escaping.
     */
    private static final byte DEFAULT_ESCAPE = 10;
    /**
     * Start of Text. Used as the default byte to indicate the start of the packet.
     */
    private static final byte DEFAULT_START = 2;
    /**
     * End of Text. Used as the default byte to indicate the end of the packet.
     */
    private static final byte DEFAULT_END = 3;

    // Attributes

    /**
     * Byte used to escape
     */
    private final byte escape;
    /**
     * Byte used to indicate the start of the packet
     */
    private final byte start;
    /**
     * Byte used to indicate the end of the packet
     */
    private final byte end;

    /**
     * Constructs an encoder using the default bytes to delimit frames
     * @see MessageToByteEncoder#MessageToByteEncoder()
     */
    public ByteStuffingEncoder() {
        this(DEFAULT_ESCAPE, DEFAULT_START, DEFAULT_END);
    }

    /**
     * Constructs an encoder using the default bytes to delimit frames
     * @see MessageToByteEncoder#MessageToByteEncoder(Class)
     */
    public ByteStuffingEncoder(Class<? extends ByteBuf> outboundMessageType) {
        this(outboundMessageType, DEFAULT_ESCAPE, DEFAULT_START, DEFAULT_END);
    }

    /**
     * Constructs an encoder using the default bytes to delimit frames
     * @see MessageToByteEncoder#MessageToByteEncoder(boolean)
     */
    public ByteStuffingEncoder(boolean preferDirect) {
        this(preferDirect, DEFAULT_ESCAPE, DEFAULT_START, DEFAULT_END);
    }

    /**
     * Constructs an encoder using the default bytes to delimit frames
     * @see MessageToByteEncoder#MessageToByteEncoder(Class, boolean)
     */
    public ByteStuffingEncoder(Class<? extends ByteBuf> outboundMessageType,
                               boolean preferDirect) {
        this(outboundMessageType, preferDirect, DEFAULT_ESCAPE, DEFAULT_START, DEFAULT_END);
    }

    /**
     * Constructs an encoder using the given bytes to delimit frames
     *
     * @param escape byte used to escape
     * @param start byte used to indicate the end of the packet
     * @param end byte used to indicate the end of the packet
     * @see MessageToByteEncoder#MessageToByteEncoder()
     */
    public ByteStuffingEncoder(byte escape,
                               byte start,
                               byte end) {
        this.escape = escape;
        this.start = start;
        this.end = end;
    }

    /**
     * Constructs an encoder using the given bytes to delimit frames
     *
     * @param escape byte used to escape
     * @param start byte used to indicate the end of the packet
     * @param end byte used to indicate the end of the packet
     * @see MessageToByteEncoder#MessageToByteEncoder(Class)
     */
    public ByteStuffingEncoder(Class<? extends ByteBuf> outboundMessageType,
                               byte escape,
                               byte start,
                               byte end) {
        super(outboundMessageType);

        this.escape = escape;
        this.start = start;
        this.end = end;
    }

    /**
     * Constructs an encoder using the given bytes to delimit frames
     *
     * @param escape byte used to escape
     * @param start byte used to indicate the end of the packet
     * @param end byte used to indicate the end of the packet
     * @see MessageToByteEncoder#MessageToByteEncoder(boolean)
     */
    public ByteStuffingEncoder(boolean preferDirect,
                               byte escape,
                               byte start,
                               byte end) {
        super(preferDirect);

        this.escape = escape;
        this.start = start;
        this.end = end;
    }

    /**
     * Constructs an encoder using the given bytes to delimit frames
     *
     * @param escape byte used to escape
     * @param start byte used to indicate the end of the packet
     * @param end byte used to indicate the end of the packet
     * @see MessageToByteEncoder#MessageToByteEncoder(Class, boolean)
     */
    public ByteStuffingEncoder(Class<? extends ByteBuf> outboundMessageType,
                               boolean preferDirect,
                               byte escape,
                               byte start,
                               byte end) {
        super(outboundMessageType, preferDirect);

        this.escape = escape;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ByteBuf msg,
                          ByteBuf out) throws Exception {

        out.writeByte(escape);
        out.writeByte(start);

        // We need to make sure that inside the packet itself
        // there are no sequences of <escape><start> or <escape><end>
        // so that the decoder can correctly identify the frame
        //
        // We avoid the issue all together by duplicating every
        // escape byte. The decoder will need to correctly handle
        // the deduplication

        while(msg.isReadable()) {
            final byte b = msg.readByte();
            if(b == escape)
                out.writeByte(escape);
            out.writeByte(b);
        }

        out.writeByte(escape);
        out.writeByte(end);
    }
}
