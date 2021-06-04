package tqs.medex.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class Supplier {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String password;

  private double lat;

  private double lon;
}
