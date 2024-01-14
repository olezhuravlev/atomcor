package local.interviews.homework.exercise;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
public class JpegValidatorServiceImpl implements ValidatorService {
    
    private final Byte[] jpegMarker;
    private final AtomicInteger idx;
    
    public JpegValidatorServiceImpl() {
        this.jpegMarker = new Byte[] { (byte) 0xff, (byte) 0xd8 };
        this.idx = new AtomicInteger();
    }
    
    private boolean isEmpty(Stream<Byte> data) {
        return data.findAny().isEmpty(); // terminal
    }
    
    private boolean isJpeg(Stream<Byte> data) {
        Stream<Byte> strm = data.limit(2); // intermediate.
        Byte[] marker = strm.toArray(Byte[]::new); // intermediate.
        return Arrays.equals(marker, jpegMarker);
    }
    
    @Override
    public Stream<Byte> validate(Stream<Byte> data) throws Exception {
        
        Supplier<Stream<Byte>> data2 = () -> data;
        
        // Length check.
        if (isEmpty(data2.get())) {
            throw new IllegalArgumentException("Data stream is empty");
        }

        // Chain of responsibility to check format.
        if (isJpeg(data2.get())) {
            // It's ok.
        } else {
            throw new IllegalArgumentException("Unknown data stream format");
        }
        
        return data.map(this::checkLength);
    }
    
    private Byte checkLength(Byte data) throws IllegalStateException {
        int counter = idx.getAndIncrement();
        if (counter > 1024 * 1024) {
            throw new IllegalStateException("Data stream is too long");
        }
        return data;
    }
}
