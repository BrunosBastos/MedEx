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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseProductPK that = (PurchaseProductPK) o;
        return product == that.product &&
                purchase == that.purchase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, purchase);
    }
}
