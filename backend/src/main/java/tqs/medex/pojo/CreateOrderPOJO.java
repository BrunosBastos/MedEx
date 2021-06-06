package tqs.medex.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class CreateOrderPOJO {

    private double lat;

    private double lon;

    // products with the corresponding amount
    private Map<Integer, Integer> products;

}
