package eu.lycoris.spring.common;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.LockModeType;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface LycorisPsqlWriteRepository<C> extends Repository<C, UUID> {

  @Query(value = "select cast(pg_advisory_xact_lock(:id) as varchar)", nativeQuery = true)
  void createTransactionLock(@Param("id") @NotNull Long id);

  @Query(value = "select cast(pg_advisory_lock(:id) as varchar)", nativeQuery = true)
  void createSessionLock(@Param("id") @NotNull Long id);

  @Query(value = "select cast(pg_advisory_unlock(:id) as varchar)", nativeQuery = true)
  void deleteSessionLock(@Param("id") @NotNull Long id);

  @NotNull @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<C> findForUpdateById(@NotNull UUID id);

  @NotNull @Modifying
  Optional<C> save(@NotNull C entity);

  @Modifying
  @NotNull Optional<C> saveAndFlush(@NotNull C entity);

  @NotNull @Modifying
  List<C> saveAll(@NotNull Iterable<C> entities);

  void flush();

  @Modifying
  void delete(@NotNull C entity);
}
