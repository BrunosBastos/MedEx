package tqs.medex.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class Supplier {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  private double lat;

  private double lon;

  public Supplier(String name, double lat, double lon) {
    this.name = name;
    this.lat = lat;
    this.lon = lon;
  }

  public Supplier() {}
}
