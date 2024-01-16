package local.interviews.homework.exercise;

import java.util.concurrent.atomic.AtomicInteger;
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
    
    @Override
    public Stream<DataHolder> validate(Stream<Byte> data) throws Exception {
        
        idx.set(0);
        
        return Stream.concat(data.sequential().map(aByte -> new DataHolder(idx.incrementAndGet(), aByte, false)).peek(dataHolder -> {
            if (dataHolder.getSequenceNumber() == 1 && !jpegMarker[0].equals(dataHolder.getAByte())) {
                throw new IllegalArgumentException("Unknown data stream format");
            }
            if (dataHolder.getSequenceNumber() == 2 && !jpegMarker[1].equals(dataHolder.getAByte())) {
                throw new IllegalArgumentException("Unknown data stream format");
            }
            if (dataHolder.getSequenceNumber() > 1024 * 1024) {
                throw new IllegalStateException("Data stream is too long");
            }
        }), Stream.of(new DataHolder(0, null, true)));
    }
}
