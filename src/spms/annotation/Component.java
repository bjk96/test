package spms.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// modify
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
  String value() default "";
}
