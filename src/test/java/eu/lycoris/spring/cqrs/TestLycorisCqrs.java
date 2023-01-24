package eu.lycoris.spring.cqrs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.lycoris.spring.cqrs.model.Command;
import eu.lycoris.spring.cqrs.model.TestCommand;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class TestLycorisCqrs {

  private final @NotNull ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void command_serialization_then_deserialization() throws JsonProcessingException {
    TestCommand command = TestCommand.builder().var1("value1").var2("value2").build();

    String json = this.objectMapper.writeValueAsString(command);

    Command cmd = this.objectMapper.readValue(json, Command.class);

    assertEquals(cmd.getClass(), TestCommand.class);
  }

  @Test
  void command_parent_properties_stay_with_serialization() throws JsonProcessingException {
    TestCommand command = TestCommand.builder().var1("value1").var2("value2").build();
    command.setMethodName("command_id_dont_change_with_serialization");
    command.setServiceClass(TestLycorisCqrs.class);
    command.setId(UUID.randomUUID());

    String json = this.objectMapper.writeValueAsString(command);

    Command cmd = this.objectMapper.readValue(json, Command.class);

    assertNotNull(cmd.getId());

    assertNotNull(cmd.getMethodName());

    assertNotNull(cmd.getServiceClass());

    assertEquals(cmd.getId(), command.getId());
  }
}
