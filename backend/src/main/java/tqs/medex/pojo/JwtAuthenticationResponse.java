package tqs.medex.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private boolean isSuperUser;

  public JwtAuthenticationResponse(String accessToken) {
    this.accessToken = accessToken;
    this.isSuperUser = false;
  }

  public JwtAuthenticationResponse(String accessToken, boolean isSuperUser) {
    this.accessToken = accessToken;
    this.isSuperUser = isSuperUser;
  }
}
