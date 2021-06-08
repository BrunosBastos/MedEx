package tqs.medex.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @AllArgsConstructor
public class CreateOrderPOJO {

    private double lat;

    private double lon;

    // products with the corresponding amount
    private Map<Long, Integer> products;

}
