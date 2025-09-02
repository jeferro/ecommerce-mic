package com.jeferro.products.reviews.reviews.infrastructure.mongo.services;

import com.jeferro.products.reviews.reviews.domain.models.ReviewCriteria;
import com.jeferro.products.reviews.reviews.domain.models.ReviewOrder;
import com.jeferro.shared.ddd.infrastructure.mongo.services.QueryMongoCreator;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewQueryMongoCreator extends QueryMongoCreator<ReviewOrder, ReviewCriteria> {

  @Override
  protected List<Criteria> mapFilter(ReviewCriteria reviewCriteria) {
	var criteria = new ArrayList<Criteria>();

	if (reviewCriteria.hasEntityId()) {
	  var domainCriteria = new Criteria().andOperator(
		  Criteria.where("entityId.domain").is(reviewCriteria.getEntityId().getDomain()),
		  Criteria.where("entityId.id").is(reviewCriteria.getEntityId().getId())
	  );

	  criteria.add(domainCriteria);
	}

	return criteria;
  }

  @Override
  protected String mapOrder(ReviewOrder order) {
	if (order == null) {
	  return "name";
	}

	return switch (order) {
	  case ID -> "_id";
	};
  }
}
