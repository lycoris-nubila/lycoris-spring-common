package eu.lycoris.spring.graphql;

import io.leangen.graphql.annotations.types.GraphQLDirective;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@GraphQLDirective(name = "key")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FederationKeyDirective {
  @NotNull String fields();
}
