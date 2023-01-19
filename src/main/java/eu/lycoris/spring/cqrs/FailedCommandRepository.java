package eu.lycoris.spring.cqrs;

import eu.lycoris.spring.cqrs.model.FailedCommand;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Repository
public interface FailedCommandRepository extends CrudRepository<FailedCommand, UUID> {

  @NotNull
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<FailedCommand> findAll();
}
