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

import java.util.Collections;

@Service
public class ExternalService {

  @Value("${app.deliveryHost:localhost}")
  private String deliveryHost;

  @Value("${app.myHost:localhost}")
  private String myHost;

  @Autowired private RestTemplate restTemplate;

  public void createDelivery(Purchase purchase) {
    try {

      var url = String.format("http://%s:8081/api/v1/deliveries", deliveryHost);
      var headers = new HttpHeaders();
      var newDelivery =
          new DeliveryPOJO(
                  String.format("http://%s:8080/api/v1/purchases", myHost),
              purchase.getId(),
              purchase.getLat(),
              purchase.getLon());
      var mapper = new ObjectMapper();
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request =
          new HttpEntity<>(mapper.writeValueAsString(newDelivery), headers);

      restTemplate.postForObject(url, request, String.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public ReviewPOJO createReview(ReviewRequestPOJO reviewRequestPOJO) {

    try {
      var url = String.format("http://%s:8081/api/v1/deliveries/reviews", deliveryHost);
      var headers = new HttpHeaders();
      reviewRequestPOJO.setHost(String.format("http://%s:8080/api/v1/purchases", myHost));
      var mapper = new ObjectMapper();
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request =
          new HttpEntity<>(mapper.writeValueAsString(reviewRequestPOJO), headers);

      return restTemplate.postForObject(url, request, ReviewPOJO.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ReviewPOJO getReview(Long purchaseId) {
    var url = String.format("http://%s:8081/api/v1/deliveries/%d/reviews", deliveryHost, purchaseId);

    return restTemplate.getForObject(url, ReviewPOJO.class);
  }
}
