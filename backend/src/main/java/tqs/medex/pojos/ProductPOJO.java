package tqs.medex.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPOJO {

    private String name;

    private int stock;

    private double price;

    private long supplier;
}
