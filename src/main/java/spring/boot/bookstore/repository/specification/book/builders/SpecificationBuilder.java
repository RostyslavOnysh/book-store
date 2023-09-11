package spring.boot.bookstore.repository.specification.book.builders;

import org.springframework.data.jpa.domain.Specification;
import spring.boot.bookstore.dto.book.BookSearchParameter;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameter searchParametersDto);
}
