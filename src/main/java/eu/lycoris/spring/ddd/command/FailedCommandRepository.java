package eu.lycoris.spring.ddd.command;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.NEVER)
public interface FailedCommandRepository extends CrudRepository<FailedCommand, UUID> {}
