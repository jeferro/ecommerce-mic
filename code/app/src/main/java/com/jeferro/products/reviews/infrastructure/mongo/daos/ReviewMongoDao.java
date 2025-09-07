package com.jeferro.products.reviews.infrastructure.mongo.daos;

import com.jeferro.products.reviews.infrastructure.mongo.dtos.ReviewMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewMongoDao extends MongoRepository<ReviewMongoDTO, String> {

}
