package com.gongxiang.zhenxuan.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 网关服务启动类
 * 
 * @author gongxiang-zhenxuan
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * 网关配置类
     */
    @Configuration
    public static class GatewayConfig {

        /**
         * 配置CORS跨域
         */
        @Bean
        public CorsWebFilter corsFilter() {
            CorsConfiguration config = new CorsConfiguration();
            
            // 允许所有域名进行跨域调用
            config.addAllowedOriginPattern("*");
            
            // 允许所有请求头
            config.addAllowedHeader("*");
            
            // 允许所有请求方法
            config.addAllowedMethod("*");
            
            // 允许携带认证信息
            config.setAllowCredentials(true);
            
            // 预检请求的缓存时间（秒）
            config.setMaxAge(3600L);
            
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
            source.registerCorsConfiguration("/**", config);
            
            return new CorsWebFilter(source);
        }
    }

    /**
     * 健康检查控制器
     */
    @RestController
    @RequestMapping("/health")
    public static class HealthController {

        /**
         * 健康检查接口
         */
        @GetMapping
        public Mono<Map<String, Object>> health() {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "UP");
            result.put("service", "gxz-gateway");
            result.put("timestamp", LocalDateTime.now());
            result.put("version", "1.0.0");
            
            return Mono.just(result);
        }

        /**
         * 就绪检查接口
         */
        @GetMapping("/ready")
        public Mono<Map<String, Object>> ready() {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "READY");
            result.put("service", "gxz-gateway");
            result.put("timestamp", LocalDateTime.now());
            
            return Mono.just(result);
        }

        /**
         * 存活检查接口
         */
        @GetMapping("/live")
        public Mono<Map<String, Object>> live() {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "ALIVE");
            result.put("service", "gxz-gateway");
            result.put("timestamp", LocalDateTime.now());
            
            return Mono.just(result);
        }
    }

    /**
     * 全局请求日志过滤器
     */
    @Slf4j
    @Component
    public static class GlobalRequestLogFilter implements GlobalFilter, Ordered {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            URI uri = request.getURI();
            String method = request.getMethod().name();
            String path = uri.getPath();
            String query = uri.getQuery();
            String remoteAddr = getRemoteAddr(request);
            
            // 记录请求开始时间
            long startTime = System.currentTimeMillis();
            String requestTime = LocalDateTime.now().format(FORMATTER);
            
            log.info("[Gateway Request] {} {} {} {} {} Query: {}", 
                    requestTime, remoteAddr, method, path, 
                    request.getHeaders().getFirst("User-Agent"), 
                    query != null ? query : "N/A");
            
            return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    int statusCode = exchange.getResponse().getStatusCode() != null ? 
                        exchange.getResponse().getStatusCode().value() : 0;
                    
                    log.info("[Gateway Response] {} {} {} {} {}ms Status: {}", 
                            requestTime, remoteAddr, method, path, duration, statusCode);
                })
            );
        }

        private String getRemoteAddr(ServerHttpRequest request) {
            String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }
            
            String xRealIp = request.getHeaders().getFirst("X-Real-IP");
            if (xRealIp != null && !xRealIp.isEmpty()) {
                return xRealIp;
            }
            
            return request.getRemoteAddress() != null ? 
                request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE;
        }
    }

    /**
     * 认证过滤器
     */
    @Slf4j
    @Component
    public static class AuthFilter implements GlobalFilter, Ordered {

        private static final List<String> SKIP_AUTH_PATHS = Arrays.asList(
            "/api/user/login",
            "/api/user/register",
            "/api/user/captcha",
            "/api/merchant/login",
            "/api/merchant/register",
            "/api/goods/list",
            "/api/goods/detail",
            "/api/content/banner",
            "/api/content/notice",
            "/actuator",
            "/health",
            "/favicon.ico"
        );

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            
            if (shouldSkipAuth(path)) {
                return chain.filter(exchange);
            }
            
            String authHeader = request.getHeaders().getFirst("Authorization");
            
            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header for path: {}", path);
                return unauthorized(exchange);
            }
            
            String token = authHeader.substring(7);
            
            if (!isValidToken(token)) {
                log.warn("Invalid JWT token for path: {}", path);
                return unauthorized(exchange);
            }
            
            String userId = extractUserIdFromToken(token);
            String userType = extractUserTypeFromToken(token);
            
            if (StringUtils.hasText(userId)) {
                ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Type", userType)
                    .build();
                
                exchange = exchange.mutate().request(modifiedRequest).build();
            }
            
            return chain.filter(exchange);
        }

        private boolean shouldSkipAuth(String path) {
            return SKIP_AUTH_PATHS.stream().anyMatch(skipPath -> 
                path.startsWith(skipPath) || path.contains(skipPath));
        }

        private boolean isValidToken(String token) {
            return StringUtils.hasText(token) && token.length() > 10;
        }

        private String extractUserIdFromToken(String token) {
            return "user123";
        }

        private String extractUserTypeFromToken(String token) {
            return "CUSTOMER";
        }

        private Mono<Void> unauthorized(ServerWebExchange exchange) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            
            String body = "{\"code\":401,\"message\":\"Unauthorized\",\"data\":null}";
            return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE + 1;
        }
    }

    /**
     * 限流过滤器
     */
    @Slf4j
    @Component
    public static class RateLimitFilter implements GlobalFilter, Ordered {

        private static final int MAX_REQUESTS_PER_MINUTE = 100;
        private static final int WINDOW_SIZE_MINUTES = 1;
        private final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            String clientIp = getClientIp(request);
            String path = request.getURI().getPath();
            
            if (isRateLimited(clientIp, path)) {
                log.warn("Rate limit exceeded for IP: {} on path: {}", clientIp, path);
                return rateLimitExceeded(exchange);
            }
            
            return chain.filter(exchange);
        }

        private boolean isRateLimited(String clientIp, String path) {
            String key = clientIp + ":" + path;
            RequestCounter counter = requestCounters.computeIfAbsent(key, k -> new RequestCounter());
            
            LocalDateTime now = LocalDateTime.now();
            
            if (counter.getWindowStart().plus(WINDOW_SIZE_MINUTES, ChronoUnit.MINUTES).isBefore(now)) {
                counter.reset(now);
            }
            
            int currentCount = counter.increment();
            return currentCount > MAX_REQUESTS_PER_MINUTE;
        }

        private String getClientIp(ServerHttpRequest request) {
            String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }
            
            String xRealIp = request.getHeaders().getFirst("X-Real-IP");
            if (xRealIp != null && !xRealIp.isEmpty()) {
                return xRealIp;
            }
            
            return request.getRemoteAddress() != null ? 
                request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        }

        private Mono<Void> rateLimitExceeded(ServerWebExchange exchange) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            response.getHeaders().add("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS_PER_MINUTE));
            response.getHeaders().add("X-RateLimit-Remaining", "0");
            response.getHeaders().add("X-RateLimit-Reset", String.valueOf(System.currentTimeMillis() + 60000));
            
            String body = "{\"code\":429,\"message\":\"Too Many Requests\",\"data\":null}";
            return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
        }

        @Override
        public int getOrder() {
            return Ordered.HIGHEST_PRECEDENCE + 2;
        }

        private static class RequestCounter {
            private final AtomicInteger count = new AtomicInteger(0);
            private volatile LocalDateTime windowStart = LocalDateTime.now();

            public int increment() {
                return count.incrementAndGet();
            }

            public void reset(LocalDateTime newWindowStart) {
                count.set(0);
                windowStart = newWindowStart;
            }

            public LocalDateTime getWindowStart() {
                return windowStart;
            }
        }
    }

    /**
     * 全局异常处理器
     */
    @Slf4j
    @Order(-1)
    @Component
    public static class GlobalExceptionHandler implements ErrorWebExceptionHandler {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
            ServerHttpResponse response = exchange.getResponse();
            
            if (response.isCommitted()) {
                return Mono.error(ex);
            }
            
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            
            HttpStatus status;
            String message;
            
            if (ex instanceof NotFoundException) {
                status = HttpStatus.NOT_FOUND;
                message = "服务不可用";
                log.error("Service not found: {}", ex.getMessage());
            } else if (ex instanceof ResponseStatusException) {
                ResponseStatusException responseStatusException = (ResponseStatusException) ex;
                status = responseStatusException.getStatus();
                message = responseStatusException.getReason();
                log.error("Response status exception: {}", ex.getMessage());
            } else if (ex instanceof java.net.ConnectException) {
                status = HttpStatus.SERVICE_UNAVAILABLE;
                message = "服务连接失败";
                log.error("Service connection failed: {}", ex.getMessage());
            } else if (ex instanceof java.util.concurrent.TimeoutException) {
                status = HttpStatus.GATEWAY_TIMEOUT;
                message = "服务响应超时";
                log.error("Service timeout: {}", ex.getMessage());
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                message = "网关内部错误";
                log.error("Gateway internal error: ", ex);
            }
            
            response.setStatusCode(status);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", status.value());
            result.put("message", message);
            result.put("data", null);
            result.put("timestamp", System.currentTimeMillis());
            result.put("path", exchange.getRequest().getPath().value());
            
            try {
                String body = objectMapper.writeValueAsString(result);
                DataBufferFactory bufferFactory = response.bufferFactory();
                return response.writeWith(Mono.just(bufferFactory.wrap(body.getBytes())));
            } catch (JsonProcessingException e) {
                log.error("Error writing response", e);
                return Mono.error(e);
            }
        }
    }
}