package local.interviews.homework.exercise.validators;

import java.util.stream.Stream;

public interface Validator {
    Stream<Byte> validate(Stream<Byte> data);
}
