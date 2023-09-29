package org.owasp.webgoat.lessons.deserialization;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.dummy.insecure.framework.VulnerableTaskHolder;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.owasp.webgoat.container.SecretsApi;
import org.owasp.webgoat.container.assignments.AssignmentEndpointTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
class DeserializeTest extends AssignmentEndpointTest {

  private MockMvc mockMvc;

  private static String OS = System.getProperty("os.name").toLowerCase();

  @BeforeEach
  void setup() {
    InsecureDeserializationTask insecureTask = new InsecureDeserializationTask();
    init(insecureTask);
    this.mockMvc = standaloneSetup(insecureTask).build();
  }

  @Test
  void success() throws Exception {
    if (OS.indexOf("win") > -1) {
      mockMvc
          .perform(
              MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                  .param(
                      "token",
                      SerializationHelper.toString(
                          new VulnerableTaskHolder("wait", "ping localhost -n 5"))))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.lessonCompleted", is(true)));
    } else {
      mockMvc
          .perform(
              MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                  .param(
                      "token",
                      SerializationHelper.toString(new VulnerableTaskHolder("wait", "sleep 5"))))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.lessonCompleted", is(true)));
    }
  }

  @Test
  void fail() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/InsecureDeserialization/task")
                .param(
                    "token",
                    SerializationHelper.toString(new VulnerableTaskHolder("delete", "rm *"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  void wrongVersion() throws Exception {
    String token = SecretsApi.getSecret("dtokenwrong");
    mockMvc
        .perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(
                    pluginMessages.getMessage("insecure-deserialization.invalidversion"))))
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  void expiredTask() throws Exception {
    String token = SecretsApi.getSecret("dtokenexpiry");
    mockMvc
        .perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(pluginMessages.getMessage("insecure-deserialization.expired"))))
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }

  @Test
  void checkOtherObject() throws Exception {
    String token = SecretsApi.getSecret("dtokencheck");
    mockMvc
        .perform(MockMvcRequestBuilders.post("/InsecureDeserialization/task").param("token", token))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.feedback",
                CoreMatchers.is(
                    pluginMessages.getMessage("insecure-deserialization.stringobject"))))
        .andExpect(jsonPath("$.lessonCompleted", is(false)));
  }
}
