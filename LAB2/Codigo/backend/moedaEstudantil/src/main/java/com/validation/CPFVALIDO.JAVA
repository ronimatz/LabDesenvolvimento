import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ValidCPF.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CPFVALIDO {
	String message() default "CPF inválido, favor verificar";
	Class <?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};
	
}
