package local.interviews.homework;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import local.interviews.homework.exercise.StreamAppender;
import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class StreamAppenderTest {
    
    private static final Byte[] empty = new Byte[0];
    private static final Byte[] notEmpty = new Byte[1];
    
    private final StreamAppender streamAppender;
    
    @BeforeAll
    public static void beforeAll() {
        Arrays.fill(notEmpty, (byte) 0x00);
    }
    
    @Test
    void validateAppendToEmpty() {
        
        Byte[] expected = new Byte[] { null };
        
        Byte[] result = streamAppender.append(Stream.of(empty)).toArray(Byte[]::new);
        assertArrayEquals(expected, result);
    }
    
    @Test
    void validateAppendToNotEmpty() {
        
        Byte[] expected = new Byte[] { (byte) 0x00, null };
        
        Byte[] result = streamAppender.append(Stream.of(notEmpty)).toArray(Byte[]::new);
        assertArrayEquals(expected, result);
    }
}
