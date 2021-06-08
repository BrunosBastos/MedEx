package tqs.medex.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private int stock;

    private double price;

    private String imageUrl;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;


    @OneToMany(mappedBy = "product")
    private Set<PurchaseProduct> purchases = new HashSet<>();

    public Product(long id, String name, String description, int stock, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Product() {
    }
}
