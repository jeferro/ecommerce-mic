package com.jeferro.products.reviews.reviews.infrastructure.mongo.daos;

import com.jeferro.products.reviews.reviews.infrastructure.mongo.dtos.ProductReviewMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewMongoDao extends MongoRepository<ProductReviewMongoDTO, String> {

    List<ProductReviewMongoDTO> findAllByProductCode(String productCode);
}
