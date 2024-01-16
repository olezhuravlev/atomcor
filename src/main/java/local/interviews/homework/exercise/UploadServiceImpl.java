package local.interviews.homework.exercise;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Заготовка для реализации (можно менять все и указывать причину изменения)
 * Менять можно все кроме самого {@link UploadService}
 * Проверка через тесты
 */
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
    
    private final StoreImpl store = new StoreImpl();
    private final ValidatorService validationService;
    
    @Override
    public Stream<Integer> upload(Stream<Byte> data) {
        try {
            return store.save(validationService.validate(data));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public static class StoreImpl {
        public Stream<Integer> save(Stream<DataHolder> data) {
            //тут саму реализацию сохранения в хранилище не реализуем,
            //только имитацию ответа на каждый килобайт данных в исходящий стрим 1024, 2048 итд...
            
            Objects.requireNonNull(data);
            
            AtomicInteger lastSequenceNumber = new AtomicInteger();
            AtomicBoolean dataDetected = new AtomicBoolean();
            
            return data.sequential().map(dataHolder -> {
                
                int sequenceNumber = dataHolder.getSequenceNumber();
                
                if (dataHolder.isEnd()) {
                    
                    if (dataDetected.get() == false) {
                        throw new IllegalArgumentException("Empty stream");
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
}
