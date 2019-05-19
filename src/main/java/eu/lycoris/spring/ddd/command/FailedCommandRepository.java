package eu.lycoris.spring.ddd.command;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedCommandRepository extends CrudRepository<FailedCommand, UUID> {}
