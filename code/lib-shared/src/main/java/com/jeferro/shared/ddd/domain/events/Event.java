package com.jeferro.shared.ddd.domain.events;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import lombok.Getter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Getter
public abstract class Event {

  protected final EventId eventId;

  public Event() {
    this.eventId = EventId.create();
  }

  public boolean hasSameId(EventId otherId) {
    return eventId.equals(otherId);
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

    var otherProjection = (Event) other;

    return hasSameId(otherProjection.eventId);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
  }
}
