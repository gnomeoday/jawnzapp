package com.jawnz.app.service;

import com.jawnz.app.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Product}.
 */
public interface ProductService {

    /**
     * Save a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    Product save(Product product);

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Product> findAll(Pageable pageable);


    /**
     * Get the "id" product.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Product> findOne(String id);

    /**
     * Delete the "id" product.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the product corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Product> search(String query, Pageable pageable);
}
