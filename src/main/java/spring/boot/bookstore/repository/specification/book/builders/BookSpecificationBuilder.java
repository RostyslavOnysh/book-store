package spring.boot.bookstore.repository.specification.book.builders;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import spring.boot.bookstore.dto.book.BookSearchParameter;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.repository.specification.book.managers.SpecificationProviderManager;

@RequiredArgsConstructor
@Component
@Tag(name = "Book Specification Builder ", description = "Builds book specifications for filtering")
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final Logger logger = LogManager.getLogger(BookSpecificationBuilder.class);
    private final SpecificationProviderManager<Book> specificationProviderManager;
    private final Map<String, Specification<Book>> specificationCache = new ConcurrentHashMap<>();

    @Operation(summary = "Build book specifications",
            description = "Builds specifications for filtering books based on search parameters.")
    @Override
    public Specification<Book> build(BookSearchParameter searchParameters) {
        List<Specification<Book>> specifications = new ArrayList<>();
        if (searchParameters.title() != null && searchParameters.title().length > 0) {
            logger.info("Filtering by title: {}", Arrays.toString(searchParameters.title()));
            specifications.add(getSpecificationFromCache("title", searchParameters.title()));
        }
        if (searchParameters.author() != null && searchParameters.author().length > 0) {
            logger.info("Filtering by author: {}", Arrays.toString(searchParameters.author()));
            specifications.add(getSpecificationFromCache("author", searchParameters.author()));
        }
        if (searchParameters.isbn() != null && searchParameters.isbn().length > 0) {
            logger.info("Filtering by isbn: {}", Arrays.toString(searchParameters.isbn()));
            specifications.add(getSpecificationFromCache("isbn", searchParameters.isbn()));
        }
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    private Specification<Book> getSpecificationFromCache(String key, String[] params) {
        return specificationCache.computeIfAbsent(key, k ->
                specificationProviderManager.getSpecificationProvider(k)
                        .getBookSpecification(params));
    }
}
