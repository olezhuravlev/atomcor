package local.interviews.homework;

import static local.interviews.homework.exercise.UploadServiceImpl.STREAM_EMPTY_MESSAGE;
import static local.interviews.homework.exercise.configurations.validators.JpgValidator.STREAM_TOO_LONG_MESSAGE;
import static local.interviews.homework.exercise.configurations.validators.JpgValidator.STREAM_UNKNOWN_FORMAT_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import local.interviews.homework.exercise.UploadService;
import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ ={@Autowired})
public class UploadServiceTests {
    private final UploadService uploadService;

    protected final MessageSource messageSource;

    private static final Byte[] invalid = new Byte[200];
    private static final Byte[] jpeg2400b = new Byte[2400];
    private static final Byte[] jpeg4096b = new Byte[4096];
    private static final Byte[] jpeg1m = new Byte[1024 * 1024];
    private static final Byte[] jpeg1mPlus = new Byte[1024 * 1024 + 1];
    private static final Integer[] ref1mOutput = new Integer[1024];
    
    private static final Byte[] jpeg400b = new Byte[400];
    private static final Integer[] jpeg400bExpected = new Integer[1];

    @BeforeAll
    public static void beforeAll() {
        
        Arrays.fill(jpeg400b, (byte) 0x00);
        makeJpegHeader(jpeg400b);
        
        Arrays.fill(jpeg2400b, (byte) 0x00);
        makeJpegHeader(jpeg2400b);

        Arrays.fill(jpeg4096b, (byte) 0x00);
        makeJpegHeader(jpeg4096b);

        Arrays.fill(invalid, (byte) 0x00);

        Arrays.fill(jpeg1m, (byte) 0x00);
        makeJpegHeader(jpeg1m);

        Arrays.fill(jpeg1mPlus, (byte) 0x00);
        makeJpegHeader(jpeg1mPlus);

        Arrays.setAll(ref1mOutput, (i) -> 1024 * (i + 1));
        
        jpeg400bExpected[0] = 400;
    }
    
    @Test
    void test400bJpeg() {
        Assertions.assertArrayEquals(jpeg400bExpected, uploadService.upload(Arrays.stream(jpeg400b))
            .toArray(Integer[]::new));
    }

    @Test
    void test1mbJpeg() {
        Assertions.assertArrayEquals(ref1mOutput, uploadService.upload(Arrays.stream(jpeg1m))
                .toArray(Integer[]::new));
    }

    @Test
    void test2400bJpeg() {
        Assertions.assertArrayEquals(new Integer[]{1024, 2048, 2400}, uploadService.upload(Arrays.stream(jpeg2400b))
                .toArray(Integer[]::new));
    }

    @Test
    void test4096bJpeg() {
        Assertions.assertArrayEquals(new Integer[]{1024, 2048, 3072, 4096}, uploadService.upload(Arrays.stream(jpeg4096b))
                .toArray(Integer[]::new));
    }

    @Test
    void testJpegInvalid() {
        
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
            () -> uploadService.upload(Arrays.stream(invalid)).count());
        
        assertEquals(messageSource.getMessage(STREAM_UNKNOWN_FORMAT_MESSAGE, null, Locale.ENGLISH), exception.getMessage());
    }

    @Test
    void test1mbPlusJpeg() {
        
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,
            () -> uploadService.upload(Arrays.stream(jpeg1mPlus)).count());
        
        assertEquals(messageSource.getMessage(STREAM_TOO_LONG_MESSAGE, null, Locale.ENGLISH), exception.getMessage());
    }

    @Test
    void testEmpty() {
        
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
            () -> uploadService.upload(Stream.empty()).count());
        
        assertEquals(messageSource.getMessage(STREAM_EMPTY_MESSAGE, null, Locale.ENGLISH), exception.getMessage());
    }
    
    private static void makeJpegHeader(Byte[] data) {
        data[0] = (byte) 0xff;
        data[1] = (byte) 0xd8;
    }
}
