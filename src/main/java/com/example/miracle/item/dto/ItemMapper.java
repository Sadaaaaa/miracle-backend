package com.example.miracle.item.dto;

import com.example.miracle.item.model.Item;

public class ItemMapper {

    public static Item fromItemDto(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getTitle(),
                itemDto.getDescription(),
                itemDto.getPrice(),
                itemDto.getOwner(),
                itemDto.getPosted(),
                null
        );
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getPrice(),
                item.getOwner(),
                item.getPosted()
        );
    }
}
