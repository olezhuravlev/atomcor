package local.interviews.homework;

public class AbstractServiceTest {
    
    protected static void makeJpegHeader(Byte[] data) {
        data[0] = (byte) 0xff;
        data[1] = (byte) 0xd8;
    }
}
