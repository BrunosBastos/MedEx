package tqs.medex.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Product {
  @Id @GeneratedValue private Long id;

  private String name;

  private int stock;

  private double price;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "supplier_id", referencedColumnName = "id")
  private Supplier supplier;

  public Product(String name, int stock, double price) {
    this.name = name;
    this.stock = stock;
    this.price = price;
  }

  public Product() {}
}
