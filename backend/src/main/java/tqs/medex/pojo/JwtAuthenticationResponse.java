package tqs.medex.pojo;

import lombok.Data;
import tqs.medex.entity.User;

@Data
public class JwtAuthenticationResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private User user;

  public JwtAuthenticationResponse(String accessToken, User user) {
    this.accessToken = accessToken;
    this.user = user;
  }
}
