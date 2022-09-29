package eu.lycoris.spring.ddd.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public abstract class Command implements Serializable {

  @Nullable @JsonIgnore @Transient private CompletableFuture<Boolean> future;
}
