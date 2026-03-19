package org.example.deliveryrouting.optimization.integration;

import lombok.RequiredArgsConstructor;
import org.example.deliveryrouting.optimization.api.dto.RouteResponseDto;
import org.example.deliveryrouting.optimization.integration.dto.PythonOptimizationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PythonOptimizationClient {

    private final RestTemplate restTemplate;

    @Value("${python.optimization.service.url:http://localhost:8000/solve}")
    private String pythonServiceUrl;

    public RouteResponseDto solveRoute(PythonOptimizationRequest request) {
        ResponseEntity<RouteResponseDto> response = restTemplate.postForEntity(
                pythonServiceUrl,
                request,
                RouteResponseDto.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }

        throw new RuntimeException("Eroare la calcularea rutei de către serviciul Python.");
    }
}
