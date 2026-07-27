package com.example.projectmanagement.Presentation.Controllers;
import com.example.projectmanagement.Application.SecurityConfig;
import com.example.projectmanagement.Application.JacksonConfig;
import com.example.projectmanagement.Application.Dtos.*;
import com.example.projectmanagement.Application.Interfaces.*;
import com.example.projectmanagement.Application.Services.*;
import com.example.projectmanagement.Presentation.Controllers.Filter.JwtFilter;
import com.example.projectmanagement.Presentation.Errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Instant; import java.util.Set;
import static org.mockito.ArgumentMatchers.anyString; import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest({ProjectController.class,UserController.class})
@Import({SecurityConfig.class,JacksonConfig.class,JwtFilter.class,ErrorWriter.class,CorrelationIdFilter.class,GlobalExceptionHandler.class})
@ImportAutoConfiguration({SecurityAutoConfiguration.class,ServletWebSecurityAutoConfiguration.class})
class ControllerWebTest {
 @Autowired MockMvc mvc; @MockitoBean ProjectCases projects; @MockitoBean UserCases users; @MockitoBean JwtService jwt;
 @Test void protectedEndpointWithoutTokenIs401() throws Exception {mvc.perform(get("/project/1")).andExpect(status().isUnauthorized()).andExpect(jsonPath("$.code").value("UNAUTHORIZED"));}
 @Test void validTokenWithoutPermissionIs403() throws Exception {when(jwt.parse(anyString())).thenReturn(new JwtClaims(7L,"a@b.com",Set.of()));mvc.perform(get("/project/1").header("Authorization","Bearer token")).andExpect(status().isForbidden()).andExpect(jsonPath("$.code").value("FORBIDDEN"));}
 @Test void permissionAllowsOwnedRead() throws Exception {when(jwt.parse(anyString())).thenReturn(new JwtClaims(7L,"a@b.com",Set.of("project:read")));when(projects.getProjectById(1,7)).thenReturn(new ProjectResponseDto(1L,"Project","desc",Instant.now(),null,Instant.now(),Instant.now().plusSeconds(60),1,1,7L));mvc.perform(get("/project/1").header("Authorization","Bearer token")).andExpect(status().isOk()).andExpect(jsonPath("$.ownerId").value(7));}
 @Test void registrationValidationIs400() throws Exception {mvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content("{}")) .andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));}
 @Test void loginIgnoresStaleAuthorizationHeader() throws Exception {when(users.loginUser(org.mockito.ArgumentMatchers.any(UserLoginDtos.class))).thenReturn(new LoginResponseDto("new-token"));mvc.perform(post("/user/login").header("Authorization","Bearer expired-token").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"mohammad.hafi@example.com\",\"password\":\"SecurePass123!\"}")) .andExpect(status().isOk()).andExpect(jsonPath("$.token").value("new-token"));}
 @Test void malformedBearerTokenIs401() throws Exception {mvc.perform(get("/project/1").header("Authorization","Basic x")).andExpect(status().isUnauthorized()).andExpect(jsonPath("$.code").value("INVALID_TOKEN"));}
}
