package com.jeferro.shared.mongo.sequence;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import com.jeferro.shared.auth.infrastructure.mongo.dtos.MetadataMongoDTO;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "sequences")
public class SequenceMongoDocument extends AuditedMongoDTO {

  private final String id;

  private final String value;

  public SequenceMongoDocument(String id, String value, String version, MetadataMongoDTO metadata) {
    super(version, metadata);
    this.id = id;
    this.value = value;
  }
}
