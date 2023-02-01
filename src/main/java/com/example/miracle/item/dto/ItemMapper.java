package com.example.miracle.item.dto;

import com.example.miracle.item.model.Item;
import com.example.miracle.user.model.User;

public class ItemMapper {

    public static Item fromItemDto(ItemDto itemDto, User owner) {
        return Item.builder()
                .id(itemDto.getId())
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .price(itemDto.getPrice())
                .owner(owner)
                .posted(itemDto.getPosted())
                .build();
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .price(item.getPrice())
                .ownerId(item.getOwner().getId())
                .posted(item.getPosted())
                .imageItems(item.getImageItems())
                .build();
    }
}
