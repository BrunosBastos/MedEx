package tqs.medex.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter
public class OrderProductPK implements Serializable {

    private long product;
    private long order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProductPK that = (OrderProductPK) o;
        return product == that.product &&
                order == that.order;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, order);
    }
}
