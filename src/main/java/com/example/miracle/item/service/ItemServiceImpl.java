package com.example.miracle.item.service;

import com.example.miracle.exception.NotFoundException;
import com.example.miracle.image.dao.ImageItemRepository;
import com.example.miracle.image.model.ImageItem;
import com.example.miracle.image.model.ImageMapper;
import com.example.miracle.image.model.ItemImage;
import com.example.miracle.item.dao.ItemRepository;
import com.example.miracle.item.dto.ItemDto;
import com.example.miracle.item.dto.ItemMapper;
import com.example.miracle.item.model.Item;
import com.example.miracle.user.dao.UserRepository;
import com.example.miracle.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Value("${upload.path.items}")
    private String uploadPath;

    private final ItemRepository itemRepository;
    private final ImageItemRepository imageItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ImageItemRepository imageItemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.imageItemRepository = imageItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto postItem(MultipartFile[] files, ItemDto itemDto) {
        List<ImageItem> imageItems = new ArrayList<>();
        List<ItemImage> itemImages = new ArrayList<>();

        if (files != null) {
            imageItems = Arrays.stream(files).map(ImageMapper::toImageItem).collect(Collectors.toList());
            itemImages = Arrays.stream(files).map(this::saveFile).collect(Collectors.toList());
        }

        User user = userRepository.findById(itemDto.getOwnerId()).orElseThrow(() -> new NotFoundException("User is not found!"));
        Item item = ItemMapper.fromItemDto(itemDto, user);
        item.setPosted(LocalDateTime.now());
        ImageMapper.linkImageToItem(imageItems, item);

//        ImageMapper.linkItemImageToItem(itemImages, item);

        Item postedItem = itemRepository.save(item);

        return ItemMapper.toItemDto(postedItem);
    }

    @Override
    public ItemDto updateItemById(Integer itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item is not found!"));

        if (itemDto.getTitle() != null) item.setTitle(itemDto.getTitle());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getPrice() != null) item.setPosted(itemDto.getPosted());
        if (itemDto.getOwnerId() != null) {
            item.setOwner(userRepository.findById(itemDto.getOwnerId()).orElseThrow(() -> new NotFoundException("User is not found!")));
        }

        Item updatedItem = itemRepository.save(item);

        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public void deleteItemById(Integer itemId) {
        itemRepository.deleteById(itemId);
        log.warn("Item " + itemId + " has been deleted!");
    }

    @Override
    public ItemDto getItemById(Integer itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item is not found!"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Page<ItemDto> getAllItems(Integer from, Integer size) {
        Page<Item> items = itemRepository.findAll(PageRequest.of(from / size, size));
//        items.forEach(i -> i.setImageItem(imageItemRepository.findAllByItem_Id(i.getId())));
        return items.map(ItemMapper::toItemDto);
    }

    @Override
    public Page<ItemDto> searchItems(String text, Integer from, Integer size) {
        Page<Item> items = itemRepository.searchItem(text, PageRequest.of(from / size, size));
//        items.forEach(i -> i.setImageItem(imageItemRepository.findAllByItem_Id(i.getId())));
        return items.map(ItemMapper::toItemDto);
    }

    @Override
    public Page<ItemDto> findAllItemsByUserId(Integer userId, Integer from, Integer size) {
        Page<Item> items = itemRepository.findItemsByOwner_Id(userId, PageRequest.of(from / size, size));
        return items.map(ItemMapper::toItemDto);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    @Override
    public ItemImage saveFile(MultipartFile file) {
        try {
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) {
                init();
            }

            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;
            String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
//        String fileName = "" + System.currentTimeMillis() + ext;
            String fileName = UUID.randomUUID() + ext;

            log.warn(fileName);

            Files.copy(file.getInputStream(), root.resolve(fileName));

            String path = uploadPath + "/" + fileName;

            return ImageMapper.toItemImage(file, path);

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
}
