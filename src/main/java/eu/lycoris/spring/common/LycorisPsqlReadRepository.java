package eu.lycoris.spring.common;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface LycorisPsqlReadRepository<C> extends Repository<C, UUID> {

  @NotNull List<C> findAll();

  @NotNull Page<C> findAll(@NotNull Pageable pageable);

  @NotNull List<C> findAll(@NotNull Sort sort);

  @NotNull Optional<C> findById(@NotNull UUID id);
}
