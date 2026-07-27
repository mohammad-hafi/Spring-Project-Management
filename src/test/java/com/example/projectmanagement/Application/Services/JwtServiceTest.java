package com.example.projectmanagement.Application.Services;
import com.example.projectmanagement.Application.JwtProperties;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import java.time.*; import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
class JwtServiceTest {
 private static final String SECRET="01234567890123456789012345678901";
 @Test void roundTripsRequiredClaimsAndPermissions(){JwtService service=new JwtService(new JwtProperties(SECRET,"issuer",Duration.ofMinutes(15)),Clock.systemUTC());String token=service.generateToken("a@b.com",7L,Set.of("project:read"));JwtClaims claims=service.parse(token);assertEquals(7,claims.userId());assertEquals(Set.of("project:read"),claims.permissions());}
 @Test void rejectsWrongIssuer(){JwtService producer=new JwtService(new JwtProperties(SECRET,"one",Duration.ofMinutes(15)),Clock.systemUTC());JwtService consumer=new JwtService(new JwtProperties(SECRET,"two",Duration.ofMinutes(15)),Clock.systemUTC());assertThrows(JwtException.class,()->consumer.parse(producer.generateToken("a@b.com",7L,Set.of())));}
 @Test void rejectsExpiredToken(){Clock old=Clock.fixed(Instant.now().minusSeconds(3600),ZoneOffset.UTC);JwtService producer=new JwtService(new JwtProperties(SECRET,"issuer",Duration.ofMinutes(1)),old);JwtService consumer=new JwtService(new JwtProperties(SECRET,"issuer",Duration.ofMinutes(15)),Clock.systemUTC());assertThrows(JwtException.class,()->consumer.parse(producer.generateToken("a@b.com",7L,Set.of())));}
}
