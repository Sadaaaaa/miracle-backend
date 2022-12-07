package com.example.miracle.item.dao;

import com.example.miracle.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("select i from Item i" +
            " where (lower(i.title) like lower(concat('%', :text, '%')) " +
            " or lower(i.description) like lower(concat('%', :text, '%'))) ")
    Page<Item> searchItem(@Param("text") String text, Pageable pageable);
}
