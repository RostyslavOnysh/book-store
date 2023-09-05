package spring.boot.bookstore.repository.specification.book.providers;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import spring.boot.bookstore.model.Book;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getBookSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get("title").in(Arrays.stream(params).toArray());
    }
}
