package com.example.miracle.item.model;

import com.example.miracle.image.model.ImageItem;
import com.example.miracle.user.model.User;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Long price;
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
    @Column(name = "posted")
    private LocalDateTime posted;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
//    @JoinColumn(name = "item_id", referencedColumnName = "id")
//    @JoinTable(name = "images_items",
//            joinColumns = @JoinColumn(name = "id"),
//            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ImageItem> imageItems;
}
