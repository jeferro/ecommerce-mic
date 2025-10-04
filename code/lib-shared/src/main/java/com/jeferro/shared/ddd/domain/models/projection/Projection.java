package com.jeferro.shared.ddd.domain.models.projection;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import com.jeferro.shared.ddd.domain.models.aggregates.Identifier;
import lombok.Getter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Getter
public class Projection<ID extends Identifier> {

  protected final ID id;

  public Projection(ID id) {
    this.id = id;
  }

  public boolean hasSameId(ID otherId) {
    return id.equals(otherId);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }

    if (this == other) {
      return true;
    }

    if (getClass() != other.getClass()) {
      return false;
    }

    var otherProjection = (Projection<ID>) other;

    return hasSameId(otherProjection.id);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
  }
}
