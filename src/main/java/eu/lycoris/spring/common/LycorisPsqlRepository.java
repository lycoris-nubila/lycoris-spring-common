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
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface LycorisPsqlRepository<I, C> extends Repository<C, UUID> {

    @NotNull
    List<I> findAll();

    @NotNull
    Page<I> findAll(@NotNull Pageable pageable);

    @NotNull
    List<I> findAll(@NotNull Sort sort);

    @NotNull
    Optional<I> findById(@NotNull UUID id);

    default @NotNull Optional<C> findForSaveById(@NotNull UUID id) {
        this.createAdvisoryLock(generateLong(id));
        return this.findForUpdateById(id);
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @NotNull
    Optional<C> findForUpdateById(@NotNull UUID id);

    default long generateLong(UUID id) {
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(id.getLeastSignificantBits());
        buffer.putLong(id.getMostSignificantBits());

        final BigInteger bi = new BigInteger(buffer.array());
        return Math.abs(bi.longValue());
    }

    @Modifying
    @NotNull
    Optional<C> save(@NotNull C entity);

    @Modifying
    @NotNull
    Optional<C> saveAndFlush(@NotNull C entity);

    void flush();

    @Modifying
    void delete(@NotNull C entity);

    @Modifying
    @NotNull
    List<C> saveAll(@NotNull Iterable<C> entities);

    @Query(value = "select cast(pg_advisory_xact_lock(:id) as varchar)", nativeQuery = true)
    void createAdvisoryLock(@NotNull @Param("id") Long id);
}
