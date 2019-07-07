package eu.lycoris.spring.ddd.domain;

import java.time.Instant;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DomainEntity.class)
public class DomainEntity_ {

  public static volatile SingularAttribute<DomainEntity<?>, ?> id;
  public static volatile SingularAttribute<DomainEntity<?>, Instant> updateDateTime;
  public static volatile SingularAttribute<DomainEntity<?>, Instant> creationDateTime;

  public static final String ID = "id";
  public static final String UPDATE_DATE_TIME = "updateDateTime";
  public static final String CREATION_DATE_TIME = "creationDateTime";
}
