package local.interviews.homework.exercise;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class StreamAppenderImpl implements StreamAppender {
    
    /**
     * Appends finalizing null element to the end of the stream (used for detection end of the stream).
     *
     * @param data stream to add elements to.
     * @return resulted stream.
     */
    @Override
    public Stream<Byte> append(Stream<Byte> data) {
        return Stream.concat(data, Stream.of(new Byte[] { null }));
    }
}
