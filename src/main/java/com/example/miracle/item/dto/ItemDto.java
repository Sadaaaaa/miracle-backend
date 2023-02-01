package com.example.miracle.item.dto;

import com.example.miracle.image.model.ImageItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price")
    private Long price;
    @JsonProperty("owner")
    private Integer ownerId;
    @JsonProperty("posted")
    private LocalDateTime posted;
    @JsonProperty("imageItems")
    private List<ImageItem> imageItems;
}
