package spring.boot.bookstore.dto;

public record BookSearchParameter(String[] title, String[] author, String[] isbn) {
}
