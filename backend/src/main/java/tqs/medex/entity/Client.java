package tqs.medex.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long clientId;

  @Column private String name;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;
}
