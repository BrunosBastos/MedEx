package tqs.medex.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CreatePurchasePOJO {

  @DecimalMin(value = "-90.00", message = "Latitude Cannot be less than -90.00")
  @DecimalMax(value = "90.00", message = "Latitude Cannot be less than 90.00")
  private double lat;

  @DecimalMin(value = "-180.00", message = "Longitude Cannot be less than -180.00")
  @DecimalMax(value = "180.00", message = "Longitude Cannot be less than 180.00")
  private double lon;
  // products with the corresponding amount
  @NotEmpty private Map<Long, Integer> products;
}
