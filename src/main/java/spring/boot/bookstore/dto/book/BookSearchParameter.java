package spring.boot.bookstore.dto.book;

public record BookSearchParameter(String[] title, String[] author, String[] isbn,
                                  String [] price, String description,String[] categories) {
}
