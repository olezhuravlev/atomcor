package local.interviews.homework;

import static local.interviews.homework.exercise.configurations.validators.JpgValidator.STREAM_TOO_LONG_MESSAGE;
import static local.interviews.homework.exercise.configurations.validators.JpgValidator.STREAM_UNKNOWN_FORMAT_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import local.interviews.homework.exercise.configurations.validators.PngValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class PngValidatorTest {
    
    private final PngValidator validator;
    private final MessageSource messageSource;
    
    private static final Byte[] empty = new Byte[0];
    private static final Byte[] wrongFormat = new Byte[3];
    private static final Byte[] correctFormat = new Byte[3];
    private static final Byte[] tooLong = new Byte[1024 * 1024 + 1];
    
    @BeforeAll
    public static void beforeAll() {
        
        Arrays.fill(wrongFormat, (byte) 0x00);
        
        Arrays.fill(correctFormat, (byte) 0x00);
        makePngHeader(correctFormat);
        
        Arrays.fill(tooLong, (byte) 0x00);
        makePngHeader(tooLong);
    }
    
    @Test
    @SneakyThrows
    void validateIsEmpty() {
        
        Byte[] expected = new Byte[] {};
        
        List<Byte> jpegBytes = validator.validate(Arrays.stream(empty)).collect(Collectors.toList());
        assertEquals(Arrays.asList(expected), jpegBytes);
    }
    
    @Test
    void validateIsNotPng() {
        
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
            () -> validator.validate(Arrays.stream(wrongFormat)).collect(Collectors.toList()));
        
        assertEquals(messageSource.getMessage(STREAM_UNKNOWN_FORMAT_MESSAGE, null, Locale.ENGLISH), exception.getMessage());
    }
    
    @Test
    @SneakyThrows
    void validateIsPng() {
        
        Byte[] expected = new Byte[3];
        makePngHeader(expected);
        
        List<Byte> jpegBytes = validator.validate(Arrays.stream(correctFormat)).collect(Collectors.toList());
        assertEquals(Arrays.asList(expected), jpegBytes);
    }
    
    @Test
    void validateIsTooLong() {
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,
            () -> validator.validate(Arrays.stream(tooLong)).collect(Collectors.toList()));
        assertEquals(messageSource.getMessage(STREAM_TOO_LONG_MESSAGE, null, Locale.ENGLISH), exception.getMessage());
    }
    
    private static void makePngHeader(Byte[] data) {
        data[0] = (byte) 0x89;
        data[1] = (byte) 0x50;
        data[2] = (byte) 0x4e;
    }
}
