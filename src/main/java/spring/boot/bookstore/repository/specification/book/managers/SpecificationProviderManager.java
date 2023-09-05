package spring.boot.bookstore.repository.specification.book.managers;

import spring.boot.bookstore.repository.specification.book.providers.SpecificationProvider;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String key);
}
