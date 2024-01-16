package local.interviews.homework;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import local.interviews.homework.exercise.validators.Validator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class JpegValidatorTest extends AbstractServiceTest {
    
    private final Validator validator;
    
    private static final Byte[] empty = new Byte[0];
    private static final Byte[] notJpeg = new Byte[2];
    private static final Byte[] jpeg = new Byte[2];
    private static final Byte[] tooLongJpeg = new Byte[1024 * 1024 + 1];
    
    @BeforeAll
    public static void beforeAll() {
        
        Arrays.fill(notJpeg, (byte) 0x00);
        
        Arrays.fill(jpeg, (byte) 0x00);
        makeJpegHeader(jpeg);
        
        Arrays.fill(tooLongJpeg, (byte) 0x00);
        makeJpegHeader(tooLongJpeg);
    }
    
    @Test
    @SneakyThrows
    public void validateIsEmpty() {
        
        List<Byte> jpegBytes = validator.validate(Arrays.stream(empty)).map(byteArr -> byteArr[0])
            .collect(Collectors.toList());
        Byte[] expected = new Byte[1];
        assertEquals(Arrays.asList(expected), jpegBytes);
    }
    
    @Test
    public void validateIsNotJpeg() {
        
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            validator.validate(Arrays.stream(notJpeg)).collect(Collectors.toList());
        });
        
        assertEquals("Unknown data stream format", exception.getMessage());
    }
    
    @Test
    @SneakyThrows
    public void validateIsJpeg() {
        
        Byte[] expected = new Byte[3];
        makeJpegHeader(expected);
        
        List<Byte> jpegBytes = validator.validate(Arrays.stream(jpeg)).map(byteArr -> byteArr[0]).collect(Collectors.toList());
        assertEquals(Arrays.asList(expected), jpegBytes);
    }
    
    @Test
    public void validateIsTooLong() {
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            validator.validate(Arrays.stream(tooLongJpeg)).collect(Collectors.toList());
        });
        assertEquals("Data stream is too long", exception.getMessage());
    }
}
