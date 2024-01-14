package local.interviews.homework.exercise;

import java.util.stream.Stream;

/**
 * Methods of the interface add necessary elements to provided stream.
 */
public interface StreamAppender {
    
    /**
     * Add necessary elements to provided stream.
     *
     * @param data stream to add elements to.
     * @return resulted stream.
     */
    Stream<Byte> append(Stream<Byte> data);
}
