package com.example.miracle.image;

import com.example.miracle.exception.NotFoundException;
import com.example.miracle.image.dao.ImageItemRepository;
import com.example.miracle.image.dao.ImageRepository;
import com.example.miracle.image.model.Image;
import com.example.miracle.image.model.ImageItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageRepository imageRepository;
    private final ImageItemRepository imageItemRepository;

    @Autowired
    public ImageController(ImageRepository imageRepository, ImageItemRepository imageItemRepository) {
        this.imageRepository = imageRepository;
        this.imageItemRepository = imageItemRepository;
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Integer id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new NotFoundException("Image is not found!"));
        return ResponseEntity.ok()
                .header("fileName", image.getFilename())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @GetMapping("/image/user/{userId}")
    public ResponseEntity<?> getImageByUserId(@PathVariable Integer userId) {
        Image image = imageRepository.findByUser_Id(userId);
        return ResponseEntity.ok(image);
//        return ResponseEntity.ok()
//                .header("fileName", image.getFilename())
//                .contentType(MediaType.valueOf(image.getContentType()))
//                .contentLength(image.getSize())
//                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @GetMapping("/image/item/{id}")
    public ResponseEntity<?> getImagesByItemId(@PathVariable Integer id) {
        List<ImageItem> images = imageItemRepository.findAllByItem_Id(id);

        List<byte[]> imgBytes = images.stream().map(ImageItem::getBytes).collect(Collectors.toList());

        return ResponseEntity.ok(images);
    }
}
