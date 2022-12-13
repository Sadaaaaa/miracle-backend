package com.example.miracle.image.model;

import com.example.miracle.item.model.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Images_items")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "filename")
    private String filename;
    @Column(name = "size")
    private Long size;
    @Column(name = "content_type")
    private String contentType;
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    @Column(name = "bytes")
    private byte[] bytes;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private Item item;

}
