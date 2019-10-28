package gov.ismonnet.netty.exceptions;

import gov.ismonnet.netty.codecs.ByteStuffingDecoder;

/**
 * Exception raised by a {@link ByteStuffingDecoder} when a frame gets discarded
 * because it's not correctly encoded
 *
 * @author Ferlo
 *
 * @see ByteStuffingDecoder
 */
public class DelimiterDecoderException extends NetworkException {

    public DelimiterDecoderException() {
    }

    public DelimiterDecoderException(String message) {
        super(message);
    }

    public DelimiterDecoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DelimiterDecoderException(Throwable cause) {
        super(cause);
    }

    public DelimiterDecoderException(String message,
                                     Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
