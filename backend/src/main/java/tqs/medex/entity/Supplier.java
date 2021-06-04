package tqs.medex.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class Supplier {
  @Id @GeneratedValue private Long id;

  private String name;

  private String password;

  private double lat;

  private double lon;

  @OneToOne(mappedBy = "supplier")
  private Product product;
}
