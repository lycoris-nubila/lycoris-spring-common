package eu.lycoris.spring.ddd.domain.vo;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class DomainVOEvent<I, D extends IDomainVO<I>> extends ApplicationEvent {

  public DomainVOEvent(D source) {
    super(source);
  }

  @Override
  @SuppressWarnings("unchecked")
  public D getSource() {
    return (D) super.getSource();
  }
}
