package com.takeaway.employeeservice.runtime.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: StMinko Date: 16.10.2019 Time: 12:30
 *
 * <p>
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = NullOrNotBlank.NullOrNotBlankValidator.class)
@Documented
public @interface NullOrNotBlank
{
  String message() default "must not be null and must contain at least one non-whitespace character!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Slf4j
  class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String>
  {
    @Override
    public void initialize(NullOrNotBlank constraintAnnotation) {
      // Nothing to do here
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
      if (value == null)
      {
        return true;
      }
      if (value.length() == 0)
      {
        return false;
      }

      boolean isAllWhitespace = value.matches("^\\s*$");
      return !isAllWhitespace;
    }
  }
}
