package tqs.medex.entity;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
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

    public Order(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.delivered = false;
        this.orderDate = new Timestamp(System.currentTimeMillis());
    }

}
