package local.interviews.homework.exercise;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import local.interviews.homework.exercise.configurations.validators.JpgValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class UploadServiceImpl implements UploadService {
    
    public static final String STREAM_EMPTY_MESSAGE = "stream-empty";
    
    private final JpgValidator validator;
    
    private final StreamAppender streamAppender;
    private final MessageSource messageSource;
    
    /**
     * Validates, prepares and saves data stream to a storage.
     *
     * @param data to store;
     * @return stream of processed data counter measured in bytes.
     */
    @Override
    public Stream<Integer> upload(Stream<Byte> data) {
        try {
            return save(streamAppender.append(validator.validate(data)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * Saves data stream to a storage.
     *
     * @param data to store;
     * @return stream of processed data counter measured in bytes.
     */
    private Stream<Integer> save(Stream<Byte> data) {
        
        Objects.requireNonNull(data);
        
        // Counter of processed bytes. Used to report amount of processed kilobytes to resulted stream.
        AtomicInteger counter = new AtomicInteger();
        AtomicBoolean dataDetected = new AtomicBoolean();
        
        return data.map(aByte -> {
            
            int count = counter.incrementAndGet();
            
            // End of the stream detected.
            if (aByte == null) {
                
                // No data yet means that the data stream is completely empty.
                if (!dataDetected.get()) {
                    throw new IllegalArgumentException(messageSource.getMessage(STREAM_EMPTY_MESSAGE, null, Locale.ENGLISH));
                }
                
                // If current counter is multiple of 1024 then we don't need to report the count
                // because it was already done on the previous byte processing iteration.
                if ((count - 1) % 1024 == 0) {
                    return null;
                } else {
                    return count - 1;
                }
            }
            
            // End of the stream not detected yet, but if we reach that line of code that means there are some payload in the stream.
            dataDetected.set(true);
            
            // Having payload byte counter multiple of 1024 we have to report about it to the result stream.
            if (count % 1024 == 0) {
                return count;
            }
            
            // All other bytes result to report null that will be filtered out on the next step of the stream processing.
            return null;
            
        }).filter(Objects::nonNull);
    }
}
