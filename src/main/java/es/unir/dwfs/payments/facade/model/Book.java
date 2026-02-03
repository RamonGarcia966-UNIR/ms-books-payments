package es.unir.dwfs.payments.facade.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para representar un libro del catálogo
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Book {

    private Long id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String category;
    private String isbn;
    private Integer rating;
    private BigDecimal price;
    private Boolean visible;
}
