package eu.lycoris.spring.ddd.command;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
