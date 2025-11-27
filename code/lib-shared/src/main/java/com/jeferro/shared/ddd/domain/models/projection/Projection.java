package com.jeferro.shared.ddd.domain.models.projection;

import com.jeferro.shared.ddd.domain.models.aggregates.Entity;
import com.jeferro.shared.ddd.domain.models.aggregates.Identifier;
import lombok.Getter;

@Getter
public abstract class Projection<ID extends Identifier> extends Entity<ID> {

  public Projection(ID id) {
    super(id);
  }
}
