package com.example.miracle.item.dto;

import com.example.miracle.user.dto.UserDto;
import com.example.miracle.user.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private User owner;
    @JsonProperty("posted")
    private LocalDateTime posted;
}
