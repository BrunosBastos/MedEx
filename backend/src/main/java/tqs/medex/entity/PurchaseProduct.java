package tqs.medex.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(PurchaseProductPK.class)
public class PurchaseProduct {

  @Id
  @ManyToOne
  @JsonIgnoreProperties("products")
  @JoinColumn(name = "purchase", referencedColumnName = "id")
  private Purchase purchase;

  @Id
  @ManyToOne
  @JoinColumn(name = "product", referencedColumnName = "id")
  private Product product;

  @Column private int productAmount;
}
