package com.example.miracle.image.dao;

import com.example.miracle.image.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {

    UserImage findByUserId(Integer userId);
}
