package eu.lycoris.spring.ddd.domain.entity.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class DomainEntityEvent<I> {

  @NotNull private final I entityId;
}
