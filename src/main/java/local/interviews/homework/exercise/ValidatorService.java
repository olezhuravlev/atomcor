package local.interviews.homework.exercise;

import java.util.stream.Stream;

public interface ValidatorService {
    Stream<DataHolder> validate(Stream<Byte> data) throws Exception;
}
