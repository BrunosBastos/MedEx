package tqs.medex.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPOJO {

  private String name;

  private String description;

  @Min(value = 0, message = "Stock Cannot be less than zero")
  private int stock;

  @Min(value = 0, message = "price cannot be less than zero")
  private double price;

  private String imageUrl;
  private long supplier;
}
