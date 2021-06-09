package tqs.medex.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table
@NoArgsConstructor
public class Purchase {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "userId")
  private User user;

  @Column private Timestamp orderDate;

  @Column private boolean delivered;

  @Column private double lat;

  @Column private double lon;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "purchase")
  private List<PurchaseProduct> products = new ArrayList<>();

  public Purchase(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
    this.delivered = false;
    this.orderDate = new Timestamp(System.currentTimeMillis());
  }
}
