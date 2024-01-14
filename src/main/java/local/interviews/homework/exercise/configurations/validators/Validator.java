package local.interviews.homework.exercise.configurations.validators;

import java.util.stream.Stream;

/**
 * Methods of the interface check stream content correctness.
 */
public interface Validator {
    /**
     * Checks stream content correctness.
     *
     * @param data stream to check;
     * @return original checked stream.
     */
    Stream<Byte> validate(Stream<Byte> data);
}
