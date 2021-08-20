package eu.lycoris.spring.graphql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.leangen.graphql.annotations.types.GraphQLDirective;

@GraphQLDirective(name = "provides")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FederationProvidesDirective {}
