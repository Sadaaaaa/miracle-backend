package com.example.miracle.image.dao;

import com.example.miracle.image.model.ImageItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageItemRepository extends JpaRepository<ImageItem, Integer> {

    List<ImageItem> findAllByItem_Id(Integer id);
}
