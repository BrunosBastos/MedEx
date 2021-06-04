package tqs.medex.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Product {
  @Id @GeneratedValue private Long id;

  private String name;

  private String description;

  private int stock;

  private double price;

  private String imageUrl;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "supplier_id", referencedColumnName = "id")
  private Supplier supplier;

  public Product(String name, String description, int stock, double price, String imageUrl) {
    this.name = name;
    this.stock = stock;
    this.price = price;
    this.description = description;
    this.imageUrl = imageUrl;
  }

  public Product() {}
}
