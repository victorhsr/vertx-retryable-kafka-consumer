package io.github.victorhsr.retry.commons.validation;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;

/**
 * Classe base para objetos que tem a capacidade
 * de se auto-validar, seguindo a especificacao JSR-380
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 **/
public abstract class SelfValidatingObject<T> {

    private Validator validator;

    private synchronized void initValidator() {
        if (Objects.nonNull(this.validator))
            return;

        this.validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    /**
     * Realiza a validacao da instancia, JSR-380
     */
    public void validateSelf(final Class<?>... groups) throws ConstraintViolationException {

        if (Objects.isNull(this.validator))
            this.initValidator();

        final Set<ConstraintViolation<T>> violations = this.validator.validate((T) this, groups);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
