package tqs.medex.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class PurchaseProductPK implements Serializable {

  private long product;
  private long purchase;

}
