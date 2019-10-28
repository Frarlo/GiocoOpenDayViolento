package gov.ismonnet.netty.codecs;

import gov.ismonnet.netty.exceptions.DelimiterDecoderException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Decodes frames delimited with the specified bytes
 *
 * @author Ferlo
 *
 * See <a href="https://en.wikipedia.org/wiki/High-Level_Data_Link_Control#Asynchronous_framing">HDLC framing</a>
 * See <a href="https://en.wikipedia.org/wiki/Consistent_Overhead_Byte_Stuffing">Byte Stuffing</a>
 */
public class ByteStuffingDecoder extends ByteToMessageDecoder {

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
     * Max length of a frame or -1 to accept any length.
     *
     * If the frame exceeds the length, it's discarded as soon as it's noticed
     * and a {@link DelimiterDecoderException} gets thrown.
     */
    private final int maxFrameLength;

    /**
     * True if the next byte to read is escaped
     */
    private boolean isEscaped;
    /**
     * True if the decoder found a valid {@link #start} and is reading the frame
     */
    private boolean isReadingFrame;
    /**
     * Frame currently being read
     */
    private ByteBuf frame;
    /**
     * Current length of {@link #frame}
     */
    private int frameLength;

    /**
     * Constructs a decoder using the default bytes to decode frames
     * accepting frames of any length
     */
    public ByteStuffingDecoder() {
        this(DEFAULT_ESCAPE, DEFAULT_START, DEFAULT_END);
    }

    /**
     * Constructs a decoder using the given bytes to decode frames
     * accepting frames of any length
     *
     * @param escape byte used to escape
     * @param start byte used to indicate the end of the packet
     * @param end byte used to indicate the end of the packet
     */
    public ByteStuffingDecoder(byte escape,
                               byte start,
                               byte end) {
        this(escape, start, end, -1);
    }

    /**
     * Constructs a decoder using the default bytes to decode frames
     * accepting frames not exceeding the given length
     *
     * If a frame exceeds the length, it's discarded as soon as it's noticed
     * and a {@link DelimiterDecoderException} gets thrown.
     *
     * @param maxFrameLength max length before a frame gets discarded or -1 to accept any length
     */
    public ByteStuffingDecoder(int maxFrameLength) {
        this(DEFAULT_ESCAPE, DEFAULT_START, DEFAULT_END, maxFrameLength);
    }

    /**
     * Constructs a decoder using the given bytes to decode frames
     * accepting frames not exceeding the given length
     *
     * If a frame exceeds the length, it's discarded as soon as it's noticed
     * and a {@link DelimiterDecoderException} gets thrown.
     *
     * @param escape byte used to escape
     * @param start byte used to indicate the end of the packet
     * @param end byte used to indicate the end of the packet
     * @param maxFrameLength max length before a frame gets discarded or -1 to accept any length
     */
    public ByteStuffingDecoder(byte escape,
                               byte start,
                               byte end,
                               int maxFrameLength) {

        this.escape = escape;
        this.start = start;
        this.end = end;
        this.maxFrameLength = maxFrameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in,
                          List<Object> out) throws Exception {

        while(in.isReadable()) {
            final byte b = in.readByte();

            if(!isEscaped) {
                if(b == escape)
                    isEscaped = true;
                else if(isReadingFrame)
                    writeToFrame(b);
            } else {
                isEscaped = false;

                if(b == start && !isReadingFrame) {

                    // Sequence of <escape><start>
                    // This indicates the start of a new frame

                    isReadingFrame = true;
                    frame = ctx.alloc().buffer();

                } else if(b == escape && isReadingFrame) {

                    // Sequence of 2 escape bytes.
                    // Deduplicate it

                    writeToFrame(b);

                } else if(b == end && isReadingFrame) {

                    // Sequence of <escape><end>
                    // This indicates the end of the frame

                    out.add(frame);
                    resetFrame();

                } else {

                    frame.release();
                    resetFrame();

                    throw new DelimiterDecoderException(String.format(
                            "There was an error while decoding packets. Discarding data." +
                                    "(currByte: %s, isEscaped: %s, isReadingFrame: %s)",
                            b, isEscaped, isReadingFrame));
                }
            }
        }
    }

    /**
     * Write the byte to {@link #frame}.
     *
     * If the frame exceeds the {@link #maxFrameLength}, a {@link DelimiterDecoderException}
     * is raised and the packet gets discarded
     *
     * @param b byte to write
     * @throws DelimiterDecoderException if the frame is exceeding the {@link #maxFrameLength}
     */
    private void writeToFrame(byte b) {

        frameLength++;
        if(maxFrameLength != -1 && frameLength > maxFrameLength) {

            frame.release();
            resetFrame();

            throw new DelimiterDecoderException(String.format(
                    "Packet length exceeds the maximum one. Discarding data." +
                            "(currentFrameLength: %s, maxFrameLength: %s)",
                    frameLength, maxFrameLength));
        } else {
            frame.writeByte(b);
        }

    }

    /**
     * Reset the frame state
     */
    private void resetFrame() {
        isReadingFrame = false;
        frame = null;
    }
}
