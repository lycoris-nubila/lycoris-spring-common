package eu.lycoris.spring.cqrs.model;

import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TestCommand extends Command {

  @NotNull String var1;

  @NotNull String var2;
}
