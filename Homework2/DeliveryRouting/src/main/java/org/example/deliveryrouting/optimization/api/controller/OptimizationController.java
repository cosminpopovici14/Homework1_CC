package org.example.deliveryrouting.optimization.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.deliveryrouting.optimization.api.dto.RouteResponseDto;
import org.example.deliveryrouting.optimization.service.OptimizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/optimization")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permitem apeluri din frontend (React)
public class OptimizationController {

    private final OptimizationService optimizationService;

    @PostMapping("/solve")
    public ResponseEntity<RouteResponseDto> solveRoute() {
        return ResponseEntity.ok(optimizationService.solve());
    }
}
