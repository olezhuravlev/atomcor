package local.interviews.homework.exercise.configurations.validators;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import lombok.RequiredArgsConstructor;

/**
 * Validates correctness of data stream applying specified marker against first bytes of the stream.
 */
@RequiredArgsConstructor
public abstract class AbstractValidator implements Validator {
    
    public static final String STREAM_UNKNOWN_FORMAT_MESSAGE = "stream-unknown-format";
    public static final String STREAM_TOO_LONG_MESSAGE = "stream-too-long";
    
    protected final MessageSource messageSource;
    
    @Value("${stream-length-max}")
    private int streamLengthMax;
    
    /**
     * Checks if the stream is compatible with provided signature.
     *
     * @param data      stream to check signature against;
     * @param signature is a few first bytes of stream enough to detect type of the stream.
     * @return
     */
    public Stream<Byte> validate(Stream<Byte> data, Byte[] signature) {
        
        AtomicInteger positionCounter = new AtomicInteger();
        
        return data.sequential().peek(aByte -> {
            
            int currentPosition = positionCounter.getAndIncrement();
            
            // Position is within marker length, so we are still able to check its correctness.
            if (currentPosition < signature.length && !signature[currentPosition].equals(aByte)) {
                throw new IllegalArgumentException(messageSource.getMessage(STREAM_UNKNOWN_FORMAT_MESSAGE, null, Locale.ENGLISH));
            }
            
            if (currentPosition >= streamLengthMax) {
                throw new IllegalStateException(messageSource.getMessage(STREAM_TOO_LONG_MESSAGE, null, Locale.ENGLISH));
            }
        });
    }
}
