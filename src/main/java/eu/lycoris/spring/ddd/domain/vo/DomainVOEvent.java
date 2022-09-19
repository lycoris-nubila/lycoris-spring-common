package eu.lycoris.spring.ddd.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class DomainVOEvent<I> {

  @NotNull private final I entityId;
}
