package local.interviews.homework.exercise;

import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import local.interviews.homework.exercise.exceptions.FileUploadException;
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
    private ValidatorService validationService;
    
    @Override
    public Stream<Integer> upload(Stream<Byte> data) {
        try {
            return store.save(validationService.validate(data));
        } catch (Exception e) {
            throw new FileUploadException(e);
        }
    }
    
    public static class StoreImpl {
        public Stream<Integer> save(Stream<Byte> data) {
            //тут саму реализацию сохранения в хранилище не реализуем,
            //только имитацию ответа на каждый килобайт данных в исходящий стрим 1024, 2048 итд...
            Objects.requireNonNull(data);
            return Stream.of(0);
        }
    }
}
