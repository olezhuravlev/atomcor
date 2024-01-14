package local.interviews.homework.exercise.configurations.validators;

import java.util.stream.Stream;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Validates correctness of jpg/jpeg data stream.
 */
@Component
public class JpgValidator extends AbstractValidator {
    
    private static final Byte[] signature = new Byte[] { (byte) 0xff, (byte) 0xd8 };
    
    public JpgValidator(MessageSource messageSource) {
        super(messageSource);
    }
    
    @Override
    public Stream<Byte> validate(Stream<Byte> data) {
        return validate(data, signature);
    }
}
