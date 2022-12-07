package com.example.miracle.image.dao;

import com.example.miracle.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Image findByUser_Id(Integer id);

}
