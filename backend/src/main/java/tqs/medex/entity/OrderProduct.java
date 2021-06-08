package tqs.medex.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table
@Getter @Setter
@AllArgsConstructor
@IdClass(OrderProductPK.class)
public class OrderProduct {

    @Id
    @ManyToOne
    @JoinColumn(name = "order", referencedColumnName = "id")
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "id")
    private Product product;

    @Column
    private int productAmount;
}
