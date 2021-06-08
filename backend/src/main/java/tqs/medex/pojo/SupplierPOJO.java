package tqs.medex.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierPOJO {

    @NotBlank
    @Size(min = 4, max = 50)
    private String name;

    @DecimalMin(value = "-90.00", message = "Latitude Cannot be less than -90.00")
    @DecimalMax(value = "90.00", message = "Latitude Cannot be less than 90.00")
    private double lat;

    @DecimalMin(value = "-180.00", message = "Longitude Cannot be less than -180.00")
    @DecimalMax(value = "180.00", message = "Longitude Cannot be less than 180.00")
    private double lon;
}
