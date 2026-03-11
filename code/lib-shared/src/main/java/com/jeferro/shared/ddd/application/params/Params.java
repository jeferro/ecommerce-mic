package com.jeferro.shared.ddd.application.params;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public abstract class Params<R> {

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    return EqualsBuilder.reflectionEquals(this, other);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
  }
}
