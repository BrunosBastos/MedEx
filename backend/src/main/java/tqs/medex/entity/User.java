package tqs.medex.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@JsonSerialize
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long userId;

  @Column private String email;

  @JsonIgnore @Column private String password;

  @Column private boolean isSuperUser;

  @Column private String name;
}
