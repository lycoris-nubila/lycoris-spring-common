package eu.lycoris.spring.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface LycorisPsqlRepository<I, C> extends Repository<C, UUID> {

  public List<I> findAll();

  public Page<I> findAll(Pageable pageable);

  public List<I> findAll(Sort sort);

  public Optional<I> findById(UUID id);

  public default Optional<C> findForSaveById(UUID id) {
    this.createAdvisoryLock(generateLong(id));
    return this.findForUpdateById(id);
  }

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public Optional<C> findForUpdateById(UUID id);

  public default long generateLong(UUID id) {
    final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
    buffer.putLong(id.getLeastSignificantBits());
    buffer.putLong(id.getMostSignificantBits());

    final BigInteger bi = new BigInteger(buffer.array());
    return Math.abs(bi.longValue());
  }

  @Modifying
  public Optional<C> save(C entity);

  @Modifying
  public Optional<C> saveAndFlush(C entity);

  public void flush();

  @Modifying
  public void delete(C entity);

  @Modifying
  public List<C> saveAll(Iterable<C> entities);

  @Query(value = "select cast(pg_advisory_xact_lock(:id) as varchar)", nativeQuery = true)
  void createAdvisoryLock(@Param("id") Long id);
}
