package local.interviews.homework.exercise.validators;

import java.util.stream.Stream;

import local.interviews.homework.exercise.DataHolder;

public interface Validator {
    Stream<DataHolder> validate(Stream<Byte> data) throws Exception;
}
