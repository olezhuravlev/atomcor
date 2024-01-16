package local.interviews.homework.exercise.validators;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JpegValidatorImpl implements Validator {
    
    private final static String UNKNOWN_DATA_STREAM_FORMAT_MESSAGE = "unknown-data-stream-format";
    private final static String DATA_STREAM_TOO_LONG_MESSAGE = "data-stream-too-long";
    
    @Value("${stream-length-max}")
    private int streamLengthMax;
    
    private final static Byte[] jpegMarker = new Byte[] { (byte) 0xff, (byte) 0xd8 };
    private final MessageSource messageSource;
    
    @Override
    public Stream<Byte[]> validate(Stream<Byte> data) {
        
        AtomicInteger idx = new AtomicInteger();
        
        return Stream.concat(data.sequential()
            .map(aByte -> new Byte[] {aByte, 0})
            .peek(byteArr -> {
                idx.incrementAndGet();
                if (idx.get() == 1 && !jpegMarker[0].equals(byteArr[0])) {
                    throw new IllegalArgumentException(messageSource.getMessage(UNKNOWN_DATA_STREAM_FORMAT_MESSAGE, null, Locale.ENGLISH));
                }
                if (idx.get() == 2 && !jpegMarker[1].equals(byteArr[0])) {
                    throw new IllegalArgumentException(messageSource.getMessage(UNKNOWN_DATA_STREAM_FORMAT_MESSAGE, null, Locale.ENGLISH));
                }
                if (idx.get() > streamLengthMax) {
                    throw new IllegalStateException(messageSource.getMessage(DATA_STREAM_TOO_LONG_MESSAGE, null, Locale.ENGLISH));
                }
            }), Stream.of((byte) 0x1).map(endFlag -> new Byte[] {null, endFlag}));
    }
}
