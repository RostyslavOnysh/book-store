package spring.boot.bookstore.repository.specification.book.managers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.repository.specification.book.providers.SpecificationProvider;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;
    private final Map<String, SpecificationProvider<Book>> providerCache =
            new ConcurrentHashMap<>();

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return providerCache.computeIfAbsent(key, this::findProviderByKey);
    }

    private SpecificationProvider<Book> findProviderByKey(String key) {
        return Optional.ofNullable(bookSpecificationProviders)
                .orElseThrow(() -> new RuntimeException("bookSpecificationProviders is null"))
                .stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Can't find correct specifcation provider for key " + key
                ));
    }
}
