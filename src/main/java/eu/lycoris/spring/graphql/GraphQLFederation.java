package eu.lycoris.spring.graphql;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;

import graphql.introspection.Introspection;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolver;
import graphql.schema.idl.SchemaPrinter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphQLFederation {
  public static GraphQLSchema createFederatedSchema(
      GraphQLSchema schema,
      BiFunction<DataFetchingEnvironment, Map<String, Object>, Object> lookup,
      TypeResolver resolve) {
    GraphQLScalarType unrepresentableScalar = (GraphQLScalarType) schema.getType("UNREPRESENTABLE");

    GraphQLDirective mappedTypeDirective =
        GraphQLDirective.newDirective()
            .name("_mappedType")
            .description("")
            .validLocation(Introspection.DirectiveLocation.OBJECT)
            .validLocation(Introspection.DirectiveLocation.INPUT_OBJECT)
            .argument(
                GraphQLArgument.newArgument()
                    .name("type")
                    .description("")
                    .type(unrepresentableScalar)
                    .build())
            .build();

    GraphQLDirective mappedOperationDirective =
        GraphQLDirective.newDirective()
            .name("_mappedOperation")
            .description("")
            .validLocation(Introspection.DirectiveLocation.FIELD_DEFINITION)
            .argument(
                GraphQLArgument.newArgument()
                    .name("operation")
                    .description("")
                    .type(unrepresentableScalar)
                    .build())
            .build();

    GraphQLDirective mappedInputFieldDirective =
        GraphQLDirective.newDirective()
            .name("_mappedInputField")
            .description("")
            .validLocation(Introspection.DirectiveLocation.INPUT_FIELD_DEFINITION)
            .argument(
                GraphQLArgument.newArgument()
                    .name("inputField")
                    .description("")
                    .type(unrepresentableScalar)
                    .build())
            .build();

    GraphQLSchema schemaWithDirectives =
        GraphQLSchema.newSchema(schema)
            .additionalDirective(mappedTypeDirective)
            .additionalDirective(mappedOperationDirective)
            .additionalDirective(mappedInputFieldDirective)
            .build();

    return implementLookupAndResolve(schemaWithDirectives, lookup, resolve);
  }

  public static void printSchema(GraphQLSchema schema) {
    String printedSchema =
        new SchemaPrinter(SchemaPrinter.Options.defaultOptions().includeDirectives(true))
            .print(schema);
    log.info(printedSchema);
  }

  private static GraphQLSchema implementLookupAndResolve(
      GraphQLSchema schema,
      BiFunction<DataFetchingEnvironment, Map<String, Object>, Object> lookup,
      TypeResolver resolve) {
    return Federation.transform(schema)
        .fetchEntities(
            env ->
                env.<List<Map<String, Object>>>getArgument(_Entity.argumentName)
                    .stream()
                    .map(values -> lookup.apply(env, values))
                    .collect(Collectors.toList()))
        .resolveEntityType(resolve)
        .build();
  }
}
