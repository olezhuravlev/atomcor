package local.interviews.homework.exercise;

import java.util.stream.Stream;

public interface ValidatorService {
    Stream<Byte> validate(Stream<Byte> data) throws Exception;
}
