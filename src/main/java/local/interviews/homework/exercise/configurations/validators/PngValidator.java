package local.interviews.homework.exercise.configurations.validators;

import java.util.stream.Stream;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Validates correctness of png data stream.
 */
@Component
public class PngValidator extends AbstractValidator {
    
    private static final Byte[] signature = new Byte[] { (byte) 0x89, (byte) 0x50, (byte) 0x4e };
    
    public PngValidator(MessageSource messageSource) {
        super(messageSource);
    }
    
    @Override
    public Stream<Byte> validate(Stream<Byte> data) {
        return validate(data, signature);
    }
}
