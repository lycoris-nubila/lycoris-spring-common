package eu.lycoris.spring.common;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.LockModeType;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface LycorisPsqlRepository<I, C> extends Repository<C, UUID> {

  @NotNull List<I> findAll();

  @NotNull Page<I> findAll(@NotNull Pageable pageable);

  @NotNull List<I> findAll(@NotNull Sort sort);

  @NotNull Optional<I> findById(@NotNull UUID id);

  @NotNull @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<C> findForUpdateById(@NotNull UUID id);

  @Modifying
  @NotNull Optional<C> save(@NotNull C entity);

  @Modifying
  @NotNull Optional<C> saveAndFlush(@NotNull C entity);

  void flush();

  @Modifying
  void delete(@NotNull C entity);

  @Modifying
  @NotNull List<C> saveAll(@NotNull Iterable<C> entities);

  @Query(value = "select cast(pg_advisory_xact_lock(:id) as varchar)", nativeQuery = true)
  void createTransactionLock(@Param("id") @NotNull Long id);

  @Query(value = "select cast(pg_advisory_lock(:id) as varchar)", nativeQuery = true)
  void createSessionLock(@Param("id") @NotNull Long id);

  @Query(value = "select cast(pg_advisory_unlock(:id) as varchar)", nativeQuery = true)
  void deleteSessionLock(@Param("id") @NotNull Long id);
}
