package com.example.Post_Service.adapter;

import com.example.Post_Service.web.dto.request.ShortUserDto;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class UserClient {
    private  final RestClient restClient;

    public UserClient(RestClient restClient) {
        this.restClient = restClient;
    }
    public List<ShortUserDto> findByIds(Set<UUID> ids){

       String token = currentToken();
        return restClient.get()
                .uri(b->b.path("/user/bath").queryParam("ids",ids).build())
                .header(HttpHeaders.AUTHORIZATION,"Bearer"+token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    private String currentToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtAuthenticationToken) auth).getToken().getTokenValue();

    }
}
