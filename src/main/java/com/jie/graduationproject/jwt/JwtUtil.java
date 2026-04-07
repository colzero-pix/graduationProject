package com.jie.graduationproject.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSecretKey() {
        if (secret == null) {
            throw new IllegalArgumentException("secret不能为空");
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    //生成jwt(外部)
    public String generateToken(String name) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, name);
    }

    //生成jwt(内部)
    public String createToken(Map<String, Object> claims, String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiration))
                .signWith(getSecretKey(),Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String Token, UserDetails userDetails) {
        try{
            final Claims claims = parserClaims(Token);

            final Date expiration = claims.getExpiration();
            final String username = claims.getSubject();
            return username.equals(userDetails.getUsername()) && !expiration.before(new Date());

        } catch (ExpiredJwtException e) {
            System.err.println("JWT令牌已过期：" + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("该JWT格式不支持" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("参数错误" + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("JWT结构错误" + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("签名验证失败" + e.getMessage());
        } catch (Exception e) {
            System.err.println("其他异常" + e.getMessage());
        }
        return false;
    }

    private Claims parserClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parserClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date getIAT(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public Date  getExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
