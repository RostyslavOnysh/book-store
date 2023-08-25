package spring.boot.bookstore;

import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.service.BookService;

@SpringBootApplication
public class IntroApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntroApplication.class);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService booksService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("First Book");
                book.setAuthor("no name");
                book.setIsbn("fgh523h");
                book.setCoverImage("beautiful nature");
                book.setPrice(BigDecimal.valueOf(999));
                booksService.save(book);
                System.out.println(booksService.findAll());
            }
        };
    }
}
