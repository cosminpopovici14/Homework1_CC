package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Policy (
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal premium
){}
