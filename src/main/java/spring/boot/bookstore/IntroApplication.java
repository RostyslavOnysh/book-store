package spring.boot.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spring.boot.bookstore.model.Book;
import spring.boot.bookstore.service.BookService;

import java.math.BigDecimal;

@SpringBootApplication
public class IntroApplication {
    @Autowired
    private BookService booksService;

    public static void main(String[] args) {
        SpringApplication.run(IntroApplication.class);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("First Book");
                book.setAuthor("no name");
                book.setPrice(BigDecimal.valueOf(999));
                booksService.save(book);

                System.out.println(booksService.findAll());
            }
        };
    }
}
