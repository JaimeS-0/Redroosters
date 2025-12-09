package com.redroosters.backend.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Duration;              // 👈 IMPORTANTE
import java.util.Date;
import java.util.function.Function;

// Genera y valida el token JWT

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Tiempo que durara el token: 7 Dias (por defecto)
    private static final long EXPIRATION_DEFAULT = Duration.ofDays(7).toMillis();

    // Tiempo que durara el token si marcaron "mantener sesión iniciada": 90 dias
    private static final long EXPIRATION_REMEMBER = Duration.ofDays(90).toMillis();

    // Crear token con el email y rememberMe
    public String generateToken(String email, boolean rememberMe) {
        long expiration = rememberMe ? EXPIRATION_REMEMBER : EXPIRATION_DEFAULT;

        Date issuedAt = new Date();
        Date expirationDate = new Date(issuedAt.getTime() + expiration);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Por si en algun sitio viejo llamas sin rememberMe
    public String generateToken(String email) {
        return generateToken(email, false);
    }

    // Extraer el email del token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validar si el token es valido
    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    // Ver si está expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Fecha de expiracion
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Metodo genérico para leer claims
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }
}
