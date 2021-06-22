package tqs.medex.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tqs.medex.entity.Purchase;
import tqs.medex.pojo.DeliveryPOJO;
import tqs.medex.pojo.ReviewPOJO;
import tqs.medex.pojo.ReviewRequestPOJO;

import java.util.Arrays;

@Service
public class ExternalService {

  @Value("${app.deliveryHost:localhost}")
  private String deliveryHost;

  @Value("${app.myHost:localhost}")
  private String myHost;

  @Autowired private RestTemplate restTemplate;

  public void createDelivery(Purchase purchase) {
    try {
      var url = "http://" + deliveryHost + ":8081/api/v1/deliveries";
      var headers = new HttpHeaders();
      var newDelivery =
              new DeliveryPOJO(
                      "http://" + myHost + ":8080/api/v1/purchases",
                      purchase.getId(),
                      purchase.getLat(),
                      purchase.getLon());
      var mapper = new ObjectMapper();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request =
              new HttpEntity<>(mapper.writeValueAsString(newDelivery), headers);

      restTemplate.postForObject(url, request, String.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public ReviewPOJO createReview(ReviewRequestPOJO reviewRequestPOJO) {

    try{
      var url = "http://" + deliveryHost + ":8081/api/v1/deliveries/reviews";
      var headers = new HttpHeaders();
      reviewRequestPOJO.setHost("http://" + myHost + ":8080/api/v1/deliveries/reviews");
      var mapper = new ObjectMapper();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request =
              new HttpEntity<>(mapper.writeValueAsString(reviewRequestPOJO), headers);

      return restTemplate.postForObject(url, request, ReviewPOJO.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
