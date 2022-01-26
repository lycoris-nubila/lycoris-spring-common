package eu.lycoris.spring.ddd.domain.vo;

public interface IDomainVO<T> {

  T getId();

  Long getVersion();
}
