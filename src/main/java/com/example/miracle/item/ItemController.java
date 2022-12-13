package com.example.miracle.item;

import com.example.miracle.item.dto.ItemDto;
import com.example.miracle.item.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/item")
    public ResponseEntity<?> postItem(@RequestParam(value = "files") MultipartFile[] files,
                                      @RequestParam(value = "item") String itemDto) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ItemDto itemDtoPojo = objectMapper.readValue(itemDto, ItemDto.class);
        return ResponseEntity.ok(itemService.postItem(files, itemDtoPojo));
    }

    @PatchMapping("/item/{itemId}")
    public ResponseEntity<?> updateItemById(@PathVariable Integer itemId,
                                            @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(itemService.updateItemById(itemId, itemDto));
    }

    @DeleteMapping("/item/{itemId}")
    public void deleteItemById(@PathVariable Integer itemId) {
        itemService.deleteItemById(itemId);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<?> getItemById(@PathVariable Integer itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @GetMapping("/items/all")
    public ResponseEntity<?> getAllItems(@RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                         @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(itemService.getAllItems(from, size));
    }

    @GetMapping("/items/search")
    public ResponseEntity<?> searchItems(@RequestParam(name = "text") String text,
                                         @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                         @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(itemService.searchItems(text, from, size));
    }

    @GetMapping("/items/user/{userId}")
    public ResponseEntity<?> findAllItemsByUserId(@PathVariable Integer userId,
                                         @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                         @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(itemService.findAllItemsByUserId(userId, from, size));
    }
}
