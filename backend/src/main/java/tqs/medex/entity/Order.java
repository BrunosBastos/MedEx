package tqs.medex.entity;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Client client;

    @Column
    private Timestamp orderDate;

    @Column
    private boolean delivered;

    @Column
    private double lat;

    @Column
    private double lon;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> products = new ArrayList<>();

}
