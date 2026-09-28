package com.example.projectmanagement;
import com.fasterxml.jackson.databind.JsonNode; import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry; import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container; import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.Instant; import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
@Testcontainers(disabledWithoutDocker=true)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT,properties={"security.jwt.secret=01234567890123456789012345678901","security.jwt.issuer=test-api"})
class ProjectApiIntegrationTest {
 @Container static PostgreSQLContainer<?> postgres=new PostgreSQLContainer<>("postgres:17-alpine");
 @DynamicPropertySource static void database(DynamicPropertyRegistry r){r.add("spring.datasource.url",postgres::getJdbcUrl);r.add("spring.datasource.username",postgres::getUsername);r.add("spring.datasource.password",postgres::getPassword);}
 @LocalServerPort int port; @Autowired ObjectMapper mapper;
 @Test void registerLoginAndManageOwnedProject() throws Exception {
  RestClient client=RestClient.builder().baseUrl("http://localhost:"+port).build();
  Map<String,Object> registration=Map.of("name","Alex Doe","description","Developer","email","alex@example.com","department","Engineering","password","Password1!","jobTitle","Developer","statusId","ACTIVE");
  ResponseEntity<String> registered=client.post().uri("/user/register").contentType(MediaType.APPLICATION_JSON).body(registration).retrieve().toEntity(String.class);assertEquals(HttpStatus.CREATED,registered.getStatusCode());assertFalse(registered.getBody().contains("Password1!"));
  String login=client.post().uri("/user/login").contentType(MediaType.APPLICATION_JSON).body(Map.of("email","alex@example.com","password","Password1!")).retrieve().body(String.class);String token=mapper.readTree(login).get("token").asText();
  Map<String,Object> project=Map.of("name","First project","description","Integration test","targetDate",Instant.now().plusSeconds(86400).toString(),"priorityLevel",1,"statusId","ACTIVE");
  String created=client.post().uri("/project").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).body(project).retrieve().body(String.class);JsonNode node=mapper.readTree(created);long id=node.get("id").asLong();assertEquals(1L,node.get("ownerId").asLong());
  String fetched=client.get().uri("/project/"+id).header("Authorization","Bearer "+token).retrieve().body(String.class);assertEquals(id,mapper.readTree(fetched).get("id").asLong());
 }
}
