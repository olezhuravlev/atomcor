package local.interviews.homework;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import local.interviews.homework.exercise.ValidatorService;
import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class JpegValidatorServiceTest {
    
    private final ValidatorService validatorService;
    
    private static final Byte[] empty = new Byte[0];
    private static final Byte[] notJpeg = new Byte[100];
    
    @BeforeAll
    public static void beforeAll() {
        Arrays.fill(notJpeg, (byte) 0x00);
    }
    
    @Test
    public void validateIsEmpty() throws Exception {
        
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            validatorService.validate(Arrays.stream(empty));
        });
        
        assertEquals("Data stream is empty", exception.getMessage());
    }
    
    @Test
    public void validateIsNotJpeg() throws Exception {
        
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            validatorService.validate(Arrays.stream(notJpeg));
        });
        
        assertEquals("Unknown data stream format", exception.getMessage());
    }
    
    //    @Test
    //    public void validateIsEmpty() throws Exception {
    //        Stream<Byte> data = Arrays.stream(jpeg1mPlus);
    //        Stream<Byte> result = validatorService.validate(data);
    //        isTrue(result.findAny().isEmpty(), "Must be");
    //    }
}
