package com.example.projectmanagement.Application.UseCase;
import com.example.projectmanagement.Application.Dtos.*;
import com.example.projectmanagement.Application.Services.JwtService;
import com.example.projectmanagement.Domain.*;
import com.example.projectmanagement.Domain.Entities.*;
import com.example.projectmanagement.Infrastructure.Repositories.*;
import com.example.projectmanagement.Infrastructure.Repositories.Users.UserAdapter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {
 @Mock UserAdapter users; @Mock StatusRepository statuses; @Mock AuthorizationRepository authorization;
 @Mock PasswordEncoder encoder; @Mock JwtService jwt; @InjectMocks UserUseCase service;
 @Test void rejectsWrongPasswordWithoutIssuingToken() {
  User user=user(); when(users.findByEmail("a@b.com")).thenReturn(Optional.of(user)); when(encoder.matches("wrong","hash")).thenReturn(false);
  assertThrows(InvalidCredentialsException.class,()->service.loginUser(new UserLoginDtos("A@B.COM","wrong")));
  verifyNoInteractions(jwt);
 }
 @Test void loginIncludesDatabasePermissions() {
  User user=user(); Set<String> permissions=Set.of("project:read");
  when(users.findByEmail("a@b.com")).thenReturn(Optional.of(user)); when(encoder.matches("valid","hash")).thenReturn(true);
  when(authorization.findPermissionTags(7)).thenReturn(permissions); when(jwt.generateToken("a@b.com",7L,permissions)).thenReturn("token");
  assertEquals("token",service.loginUser(new UserLoginDtos("A@B.COM","valid")).token());
 }
 @Test void duplicateRegistrationIsConflict() {
  when(users.existsByEmail("a@b.com")).thenReturn(true);
  assertThrows(ConflictException.class,()->service.registerUser(register()));
 }
 @Test void registrationHashesPasswordAndAssignsMember() {
  Status status=new Status(); status.setId(1); when(statuses.findById(1)).thenReturn(Optional.of(status));
  when(encoder.encode("Password1!")).thenReturn("hash"); when(users.save(any())).thenAnswer(inv->{User u=inv.getArgument(0);u.setId(7L);return u;});
  when(authorization.assignMemberRole(7)).thenReturn(1);
  UserResponseDto result=service.registerUser(register());
  assertEquals(7,result.id()); assertEquals("a@b.com",result.email()); verify(authorization).assignMemberRole(7);
 }
 private static UserRegisterDtos register(){return new UserRegisterDtos("Alex","Description","A@B.COM","Engineering","Password1!","Developer",1);}
 private static User user(){User u=new User();u.setId(7L);u.setEmail("a@b.com");u.setPassword("hash");return u;}
}
