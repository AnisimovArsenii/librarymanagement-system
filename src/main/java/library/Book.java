public class Book {

    private int id;
    private String title;
    private String author;
    private int year;
    private String isbn;
    private boolean available;

    // Конструктор
    public Book(int id, String title, String author, int year, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.isbn = isbn;
        this.available = true; // по умолчанию книга доступна
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    // Сеттер доступности
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        String status = available ? "Доступна" : "Выдана";
        return "[ID: " + id + "] \"" + title + "\" — " + author + " (" + year + ")\n"
                + "ISBN: " + isbn + " | Статус: " + status;
    }
}