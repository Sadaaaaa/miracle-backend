package com.example.miracle.item.service;

import com.example.miracle.exception.NotFoundException;
import com.example.miracle.image.model.ImageItem;
import com.example.miracle.image.model.ImageMapper;
import com.example.miracle.item.dao.ItemRepository;
import com.example.miracle.item.dto.ItemDto;
import com.example.miracle.item.dto.ItemMapper;
import com.example.miracle.item.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDto postItem(MultipartFile[] files, ItemDto itemDto) {
        List<ImageItem> imageItems = new ArrayList<>();

        if (files.length != 0) {
            imageItems = Arrays.stream(files).map(ImageMapper::toImageItem).collect(Collectors.toList());
        }

        Item item = ItemMapper.fromItemDto(itemDto);
        item.setPosted(LocalDateTime.now());
        ImageMapper.linkImageToItem(imageItems, item);

        Item postedItem = itemRepository.save(item);

        return ItemMapper.toItemDto(postedItem);
    }

    @Override
    public ItemDto updateItemById(Integer itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item is not found!"));

        if (itemDto.getTitle() != null) item.setTitle(itemDto.getTitle());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getPrice() != null) item.setPosted(itemDto.getPosted());
        if (itemDto.getOwner() != null) item.setOwner(itemDto.getOwner());

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
        return items.map(ItemMapper::toItemDto);
    }

    @Override
    public Page<ItemDto> searchItems(String text, Integer from, Integer size) {
        Page<Item> items = itemRepository.searchItem(text, PageRequest.of(from / size, size));
        return items.map(ItemMapper::toItemDto);
    }
}
