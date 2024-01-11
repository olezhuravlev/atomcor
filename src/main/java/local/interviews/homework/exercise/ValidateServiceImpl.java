package local.interviews.homework.exercise;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Заготовка для реализации (можно менять все и указывать причину изменения)
 * Менять можно все кроме самого {@link UploadService}
 * Проверка через тесты
 */
@Service
public class ValidateServiceImpl implements UploadService {
    private final StoreImpl store = new StoreImpl();

    @Override
    public Stream<Integer> upload(Stream<Byte> data) {
        return store.save(validate(data));
    }

    public Stream<Byte> validate(Stream<Byte> data) {
        //в примере валидация JPEG не реализована
        //для теста подойдет упрощенная валидация, если первые 2 байта потока равны 0xff, 0xd8 (JPEG SOI marker)
        return data;
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
