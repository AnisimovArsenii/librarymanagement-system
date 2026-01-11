import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Book> books;
    private OperationLog operationLog;

    public Library() {
        this.books = new ArrayList<>();
        this.operationLog = new OperationLog();
    }

    public void addBook(Book book) {
        books.add(book);
        operationLog.addEntry(
                OperationLog.OperationType.ADD_BOOK,
                "Добавлена книга: " + book.getTitle() + " (ID: " + book.getId() + ")"
        );
    }

    // Новый метод: Удаление книги по ID
    public boolean removeBook(int id) {
        Book bookToRemove = findBookById(id);
        if (bookToRemove != null) {
            boolean removed = books.remove(bookToRemove);
            if (removed) {
                operationLog.addEntry(
                        OperationLog.OperationType.REMOVE_BOOK,
                        "Удалена книга: " + bookToRemove.getTitle() + " (ID: " + bookToRemove.getId() + ")"
                );
                return true;
            }
        }
        return false;
    }

    public Book findBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public List<Book> findBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }
        return result;
    }

    public boolean borrowBook(int id) {
        Book book = findBookById(id);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            operationLog.addEntry(
                    OperationLog.OperationType.BORROW,
                    "Выдана книга: " + book.getTitle() + " (ID: " + book.getId() + ")"
            );
            return true;
        }
        return false;
    }

    public boolean returnBook(int id) {
        Book book = findBookById(id);
        if (book != null && !book.isAvailable()) {
            book.setAvailable(true);
            operationLog.addEntry(
                    OperationLog.OperationType.RETURN,
                    "Возвращена книга: " + book.getTitle() + " (ID: " + book.getId() + ")"
            );
            return true;
        }
        return false;
    }

    public List<Book> getAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                result.add(book);
            }
        }
        return result;
    }

    public void printOperationLog() {
        operationLog.printLog();
    }

    // Метод: Получение статистики
    public String getStatistics() {
        int totalBooks = books.size();
        int availableBooks = getAvailableBooks().size();
        int borrowedBooks = totalBooks - availableBooks;
        
        // Расчет процентов
        double availablePercentage = totalBooks > 0 ? 
            (double) availableBooks / totalBooks * 100 : 0;
        double borrowedPercentage = totalBooks > 0 ? 
            (double) borrowedBooks / totalBooks * 100 : 0;
        
        return String.format(
            "=== СТАТИСТИКА БИБЛИОТЕКИ ===\n" +
            "Всего книг: %d\n" +
            "Доступно: %d (%.1f%%)\n" +
            "Выдано: %d (%.1f%%)",
            totalBooks, availableBooks, availablePercentage, 
            borrowedBooks, borrowedPercentage
        );
    }

    // Дополнительный метод для вывода статистики
    public void printStatistics() {
        System.out.println(getStatistics());
    }

    // Геттер для получения всех книг
    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    // ===== ВЛОЖЕННЫЙ СТАТИЧЕСКИЙ КЛАСС =====
    public static class OperationLog {

        private List<LogEntry> entries = new ArrayList<>();

        public void addEntry(OperationType type, String description) {
            entries.add(new LogEntry(type, description));
        }

        public List<LogEntry> getEntries() {
            return entries;
        }

        public void printLog() {
            System.out.println("=== Журнал операций ===");
            for (LogEntry entry : entries) {
                System.out.println(entry);
            }
        }

        // Внутренний класс
        public class LogEntry {
            private OperationType type;
            private LocalDateTime timestamp;
            private String description;

            public LogEntry(OperationType type, String description) {
                this.type = type;
                this.timestamp = LocalDateTime.now();
                this.description = description;
            }

            public OperationType getType() {
                return type;
            }

            public LocalDateTime getTimestamp() {
                return timestamp;
            }

            public String getDescription() {
                return description;
            }

            @Override
            public String toString() {
                return "[" + timestamp + "] " + type + " — " + description;
            }
        }

        // Обновленный enum с добавлением типа REMOVE_BOOK
        public enum OperationType {
            ADD_BOOK, BORROW, RETURN, REMOVE_BOOK
        }
    }
}