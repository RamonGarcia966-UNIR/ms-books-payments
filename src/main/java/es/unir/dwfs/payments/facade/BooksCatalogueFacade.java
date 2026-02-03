package es.unir.dwfs.payments.facade;

import es.unir.dwfs.payments.facade.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Facade para comunicación con el microservicio de catálogo
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BooksCatalogueFacade {

    @Value("${getBook.url}")
    private String getBookUrl;

    private final WebClient.Builder webClient;

    /**
     * Obtiene un libro del catálogo por su ID
     * 
     * @param id ID del libro
     * @return Book DTO o null si no existe
     */
    public Book getBook(String id) {
        try {
            String url = String.format(getBookUrl, id);
            log.info("Getting book with ID {}. Request to {}", id, url);

            return webClient.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Book.class)
                    .block();

        } catch (HttpClientErrorException e) {
            log.error("Client Error: {}, Book with ID {}", e.getStatusCode(), id);
            return null;
        } catch (HttpServerErrorException e) {
            log.error("Server Error: {}, Book with ID {}", e.getStatusCode(), id);
            return null;
        } catch (Exception e) {
            log.error("Error: {}, Book with ID {}", e.getMessage(), id);
            return null;
        }
    }
}
