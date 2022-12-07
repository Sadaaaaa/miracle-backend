package com.example.miracle.item.service;

import com.example.miracle.item.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ItemService {
    ItemDto postItem(MultipartFile[] file, ItemDto itemDto) throws IOException;

    ItemDto updateItemById(Integer itemId, ItemDto itemDto);

    void deleteItemById(Integer itemId);

    ItemDto getItemById(Integer itemId);

    Page<ItemDto> getAllItems(Integer from, Integer size);

    Page<ItemDto> searchItems(String text, Integer from, Integer size);
}
