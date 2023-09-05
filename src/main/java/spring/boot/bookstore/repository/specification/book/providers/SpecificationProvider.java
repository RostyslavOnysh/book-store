package spring.boot.bookstore.repository.specification.book.providers;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {

    String getKey();

    Specification<T> getBookSpecification(String[] params);
}
