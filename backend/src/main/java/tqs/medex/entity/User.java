package tqs.medex.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long userId;

  @Column private String email;

  @Column private String password;
}
