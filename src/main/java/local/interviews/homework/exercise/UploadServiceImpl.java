package local.interviews.homework.exercise;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import local.interviews.homework.exercise.validators.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
    
    private final static String EMPTY_STREAM_FORMAT_MESSAGE = "empty-stream";

    private final Validator validator;
    private final MessageSource messageSource;
    
    @Override
    public Stream<Integer> upload(Stream<Byte> data) {
        try {
            return save(validator.validate(data));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    private Stream<Integer> save(Stream<DataHolder> data) {
        //тут саму реализацию сохранения в хранилище не реализуем,
        //только имитацию ответа на каждый килобайт данных в исходящий стрим 1024, 2048 итд...
        
        Objects.requireNonNull(data);
        
        AtomicInteger lastSequenceNumber = new AtomicInteger();
        AtomicBoolean dataDetected = new AtomicBoolean();
        
        return data.sequential().map(dataHolder -> {
            
            int sequenceNumber = dataHolder.getSequenceNumber();
            
            if (dataHolder.isEnd()) {
                
                if (dataDetected.get() == false) {
                    throw new IllegalArgumentException(messageSource.getMessage(EMPTY_STREAM_FORMAT_MESSAGE, null, Locale.ENGLISH));
                }
                
                int lastSeqNum = lastSequenceNumber.get();
                if (lastSeqNum % 1024 == 0) {
                    return null;
                } else {
                    return lastSeqNum;
                }
            }
            
            dataDetected.set(true);
            lastSequenceNumber.set(sequenceNumber);
            
            if (sequenceNumber % 1024 == 0) {
                return sequenceNumber;
            }
            
            return null;
            
        }).filter(Objects::nonNull);
    }
}
