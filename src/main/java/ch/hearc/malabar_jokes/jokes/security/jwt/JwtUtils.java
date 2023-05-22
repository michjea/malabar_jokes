package ch.hearc.malabar_jokes.jokes.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.hearc.malabar_jokes.JmsLogger;
import ch.hearc.malabar_jokes.jokes.model.Log;
import ch.hearc.malabar_jokes.jokes.service.impl.UserDetailsImpl;

import org.springframework.security.core.Authentication;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.*;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Autowired
    private JmsLogger jmsLogger;

    @Value("${malabar_jokes.app.jwtSecret}")
    private String jwtSecret;

    @Value("${malabar_jokes.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            jmsLogger.log(new Log("ERROR", "Invalid JWT signature: " + e));
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            jmsLogger.log(new Log("ERROR", "Invalid JWT token: " + e));
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            jmsLogger.log(new Log("ERROR", "JWT token is expired: " + e));
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            jmsLogger.log(new Log("ERROR", "JWT token is unsupported: " + e));
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            jmsLogger.log(new Log("ERROR", "JWT claims string is empty: " + e));
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}