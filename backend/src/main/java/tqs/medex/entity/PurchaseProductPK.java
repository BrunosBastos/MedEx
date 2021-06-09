package tqs.medex.entity;

import lombok.Data;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PurchaseProductPK implements Serializable {

  private long product;
  private long purchase;

}
