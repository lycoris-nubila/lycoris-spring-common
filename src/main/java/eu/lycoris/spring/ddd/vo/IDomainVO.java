package eu.lycoris.spring.ddd.vo;

public interface IDomainVO<T> {

  T getId();

  Long getVersion();
}
