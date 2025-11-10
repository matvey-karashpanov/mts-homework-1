package com.mipt.ailanakaramchakova.validationclass;

import com.mipt.ailanakaramchakova.validationclass.annotations.Email;
import com.mipt.ailanakaramchakova.validationclass.annotations.NotNull;
import com.mipt.ailanakaramchakova.validationclass.annotations.Range;
import com.mipt.ailanakaramchakova.validationclass.annotations.Size;
import com.mipt.ailanakaramchakova.validationclass.result.ValidationResult;
import java.lang.reflect.Field;


public class Validator {

  public static ValidationResult validate(Object object) {
    ValidationResult result = new ValidationResult();
    Class<?> clazz = object.getClass();

    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);

      try {
        Object value = field.get(object);

        if (field.isAnnotationPresent(NotNull.class)) {
          NotNull notNull = field.getAnnotation(NotNull.class);
          if (value == null) {
            result.addError(notNull.message());
          }
        }

        if (field.isAnnotationPresent(Size.class)) {
          Size size = field.getAnnotation(Size.class);
          if (value != null) {
            String str = (String) value;
            int length = str.length();
            if (length < size.min() || length > size.max()) {
              result.addError(size.message());
            }
          }
        }

        if (field.isAnnotationPresent(Range.class)) {
          Range range = field.getAnnotation(Range.class);
          if (value != null) {
            Integer number = (Integer) value;
            if (number < range.min() || number > range.max()) {
              result.addError(range.message());
            }
          }
        }

        if (field.isAnnotationPresent(Email.class)) {
          Email email = field.getAnnotation(Email.class);
          if (value != null) {
            String string = (String) value;
            if (!string.contains("@") || !string.contains(".")) {
              result.addError(email.message());
            }
          }
        }

      } catch (IllegalAccessException e) {
        result.addError("Cannot access field: " + field.getName());
      } catch (ClassCastException e) {
        result.addError("Annotation used on wrong field type: " + field.getName());
      }
    }

    return result;
  }
}
