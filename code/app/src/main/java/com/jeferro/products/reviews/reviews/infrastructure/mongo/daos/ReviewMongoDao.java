package com.jeferro.products.reviews.reviews.infrastructure.mongo.daos;

import com.jeferro.products.reviews.reviews.infrastructure.mongo.dtos.ReviewMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewMongoDao extends MongoRepository<ReviewMongoDTO, String> {

    List<ReviewMongoDTO> findAllByProductCode(String productCode);
}
