package gov.ismonnet.netty;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

import java.nio.charset.Charset;

/**
 * Helper class to write and read stuff in {@link ByteBuf}s
 *
 * @author Ferlo
 */
public class ByteBufHelper {

    private ByteBufHelper() {} // Limit scope

    /**
     * Write the given UTF-8 string in the buffer
     *
     * @param buf buffer to write the string in
     * @param string string to write
     */
    public static void writeString(ByteBuf buf, String string) {
        final byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    /**
     * Read a UTF-8 string from the buffer
     *
     * @param buf buffer to read the string from
     * @param maxLength max length to read (or -1 to read any length)
     * @return string read from the buffer
     * @throws DecoderException if the string length exceeded maxLength
     */
    public static String tryReadString(ByteBuf buf, int maxLength) throws DecoderException {
        final int length = buf.readInt();

        if(maxLength != -1 && maxLength < length)
            throw new DecoderException(String.format(
                    "String is too long! (lenght: %s, maxLenght: %s)",
                    length, maxLength
            ));

        return buf.readSlice(length).toString(Charset.forName("UTF-8"));
    }

    /**
     * Read a UTF-8 string from the buffer
     *
     * @param buf buffer to read the string from
     * @param maxLength max length to read (or -1 to read any length)
     * @return string read from the buffer or an empty if the string length exceeded maxLength
     */
    public static String readString(ByteBuf buf, int maxLength) {
        try {
            return tryReadString(buf, maxLength);
        } catch (DecoderException ex) {
            return "";
        }
    }

    /**
     * Read a UTF-8 string from the buffer
     *
     * @param buf buffer to read the string from
     * @return string read from the buffer
     */
    public static String readString(ByteBuf buf) {
        return readString(buf, -1);
    }
}
