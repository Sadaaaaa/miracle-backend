package com.example.miracle.image.model;

import com.example.miracle.item.model.Item;
import com.example.miracle.user.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class ImageMapper {

    public static Image toImageUser(MultipartFile file) throws IOException {
        return new Image(
                null,
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType(),
                file.getBytes(),
                null);
    }

    public static void linkImageToUser(Image image, User user) {
        image.setUser(user);
        user.setImage(image);
    }

    public static ImageItem toImageItem(MultipartFile file) {
        try {
            return new ImageItem(
                    null,
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    file.getBytes(),
                    null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void linkImageToItem(List<ImageItem> imageItems, Item item) {
        imageItems.forEach(i -> i.setItem(item));
        item.setImageItems(imageItems);
    }
}
