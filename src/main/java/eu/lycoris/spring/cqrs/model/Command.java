package eu.lycoris.spring.cqrs.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import java.util.UUID;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@type")
public class Command implements Serializable {

  @Nullable private Class<?> serviceClass;

  @Nullable private String methodName;

  @Nullable private UUID id;
}
