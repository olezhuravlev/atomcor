package local.interviews.homework.exercise;

import java.util.stream.Stream;

/**
 1) применить принципы SOLID для корректировки реализации сервиса потоковой загрузки данных

 2) реализовать поддержку JPEG, в будущем поддерживаемых форматов данных будет больше
    (если не поддерживается формат выбросить IllegalArgumentException)

 3) дополнительно реализовать валидацию на максимальную длину потока 1мб (выбросить IllegalStateException)

 4) посылать на каждый килобайт загруженных данных в выходной стрим число уже загруженных байт
    кратно 1024, последним элементом выдавать итоговое число байт
    примеры:
    при загрузке 2400 байт в выходном потоке будет 1024, 2048, 2400
    при загрузке 400 байт в выходном потоке будет 400

 5) избегать операций которые полностью буферизируют или входящий или исходящий поток

 6) Если пустой поток выбросить IllegalArgumentException

 PS
 данная задача не затрагивает моменты связанные с многопоточностью и не требует подобного анализа
 не реализует реальный пример (как это может быть сделано в реальном решении)
 воспринимать как просто задание для теста
 */
public interface UploadService {
    Stream<Integer> upload(Stream<Byte> data);
}
