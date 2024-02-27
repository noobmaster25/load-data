package com.example.loaadataspringbatch.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CsvEntry {
        private String eventTime;
        private String eventType;
        private Long productId;
        private Long categoryId;
        private String categoryCode;
        private String brand;
        private BigDecimal price;
        private Long userId;
        private String userSession;
}
