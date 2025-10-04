// package br.edu.ifpb.es.gerenciador.service; (ou onde preferir)
package br.edu.ifpb.es.gerenciador.service;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtBlacklistService {

    private final Map<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklistToken(String token, Instant expiration) {
        blacklistedTokens.put(token, expiration);
        System.out.println("Token adicionado à lista negra: " + token + " com expiração em: " + expiration);
    }

    public boolean isTokenBlacklisted(String token) {
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(Instant.now()));
        return blacklistedTokens.containsKey(token);
    }

}