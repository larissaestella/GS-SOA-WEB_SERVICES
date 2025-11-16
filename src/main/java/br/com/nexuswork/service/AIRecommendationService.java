package br.com.nexuswork.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AIRecommendationService {

    private final WebClient webClient;

    @Value("${openai.model}")
    private String model;

    private final int maxRetries;
    private final long initialBackoffMs;
    private final long cacheTtlMs;

    private final Map<String, CachedResponse> cache = new ConcurrentHashMap<>();

    public AIRecommendationService(WebClient.Builder builder,
                                   @Value("${openai.api.key}") String key,
                                   @Value("${openai.max-retries:4}") int maxRetries,
                                   @Value("${openai.initial-backoff-ms:1000}") long initialBackoffMs,
                                   @Value("${openai.cache-ttl-ms:300000}") long cacheTtlMs) {

        this.webClient = builder
                .defaultHeader("Authorization", "Bearer " + key)
                .defaultHeader("Content-Type", "application/json")
                .build();

        this.maxRetries = maxRetries;
        this.initialBackoffMs = initialBackoffMs;
        this.cacheTtlMs = cacheTtlMs;
    }


    private record CachedResponse(String text, long expireAt) {}

    private String getFromCache(String key) {
        CachedResponse v = cache.get(key);
        if (v == null) return null;

        if (Instant.now().toEpochMilli() > v.expireAt) {
            cache.remove(key);
            return null;
        }
        return v.text;
    }

    private void putCache(String key, String text) {
        cache.put(key, new CachedResponse(text, Instant.now().toEpochMilli() + cacheTtlMs));
    }


    public String askQuestionAboutCourse(Long userId, Long courseId, String question, String courseContext) {
        List<String> parts = List.of(
                "Contexto do curso: " + (courseContext == null ? "" : courseContext),
                "Usuário id=" + userId,
                "Pergunta: " + question
        );

        return askOpenAI(parts);
    }

    public String recommendForUser(int userPoints, int userLevel) {
        String base;

        if (userPoints < 1000)
            base = "Cursos introdutórios e curtos.";
        else if (userPoints < 2500)
            base = "Cursos intermediários com projetos.";
        else
            base = "Especializações e projetos aplicados.";

        return askOpenAI(List.of(
                "Usuário: pontos=" + userPoints + ", nível=" + userLevel,
                "Sugestão base: " + base,
                "Gere 3 recomendações curtas com base no usuário selecionado e 1 sugestão de pausa."
        ));
    }

    // CORE: CHAMADA PARA OPENAI

    private String askOpenAI(List<String> promptParts) {

        String cacheKey = String.join("||", promptParts);
        String cached = getFromCache(cacheKey);
        if (cached != null) return cached;


        Map<String, Object> body = Map.of(
                "model", model,   // AGORA FUNCIONA
                "messages", List.of(
                        Map.of("role", "system", "content", "Você é um assistente educacional objetivo."),
                        Map.of("role", "user", "content", String.join("\n\n", promptParts))
                ),
                "max_tokens", 500,
                "temperature", 0.7
        );


        int attempt = 0;
        long backoff = initialBackoffMs;

        while (true) {
            attempt++;

            try {
                Map response = webClient.post()
                        .uri("https://api.openai.com/v1/chat/completions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

                if (response == null) return "Erro: resposta vazia da OpenAI";

                List choices = (List) response.get("choices");
                if (choices == null || choices.isEmpty()) return "Erro: resposta inválida da OpenAI";

                Map first = (Map) choices.get(0);
                Map message = (Map) first.get("message");

                if (message == null) return "Erro: mensagem inválida da OpenAI";

                String content = message.get("content").toString().trim();

                putCache(cacheKey, content);
                return content;

            }
            catch (WebClientResponseException e) {

                return "Erro da OpenAI: " + e.getStatusCode() +
                        " - " + e.getResponseBodyAsString();
            }
            catch (Exception e) {
                if (attempt > maxRetries)
                    return "Erro ao conectar com a IA: " + e.getMessage();

                try { Thread.sleep(backoff); } catch (InterruptedException ignored) {}
                backoff *= 2;
            }
        }
    }
}
