package local.interviews.homework.exercise;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class DataHolder {
    private final int sequenceNumber;
    private final Byte aByte;
    private final boolean isEnd;
}
