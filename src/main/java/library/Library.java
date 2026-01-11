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
                "Добавлена книга: " + book.getTitle()
        );
    }

    public boolean removeBook(int id) {
        Book book = findBookById(id);
        if (book != null) {
            boolean removed = books.remove(book);
            if (removed) {
                operationLog.addEntry(
                        OperationLog.OperationType.REMOVE_BOOK,
                        "Удалена книга: " + book.getTitle()
                );
                return true;
            }
        }
        return false;
    }

    // НОВЫЙ МЕТОД: Обновление информации о книге
    public boolean updateBook(int id, Book newData) {
        Book existingBook = findBookById(id);
        if (existingBook != null) {
            // Сохраняем старое название для лога
            String oldTitle = existingBook.getTitle();
            
            // Обновляем информацию о книге
            // В реальном приложении нужно было бы добавить сеттеры в класс Book
            // или создать новый конструктор/метод для обновления
            
            // Поскольку у нас нет сеттеров в Book, я покажу альтернативный подход:
            // Удаляем старую книгу и добавляем новую с тем же ID
            books.remove(existingBook);
            
            // Создаем новую книгу с тем же ID и новыми данными
            Book updatedBook = new Book(
                id, // Сохраняем тот же ID
                newData.getTitle(),
                newData.getAuthor(),
                newData.getYear(),
                newData.getIsbn()
            );
            
            // Устанавливаем статус доступности (если он был изменен)
            updatedBook.setAvailable(existingBook.isAvailable());
            
            books.add(updatedBook);
            
            operationLog.addEntry(
                    OperationLog.OperationType.UPDATE_BOOK,
                    "Обновлена книга: '" + oldTitle + "' -> '" + newData.getTitle() + "'"
            );
            return true;
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
                    "Выдана книга: " + book.getTitle()
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
                    "Возвращена книга: " + book.getTitle()
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

    public String getStatistics() {
        int totalBooks = books.size();
        int availableBooks = getAvailableBooks().size();
        int borrowedBooks = totalBooks - availableBooks;
        
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

    // Геттер для всех книг
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

        // Enum с добавлением UPDATE_BOOK
        public enum OperationType {
            ADD_BOOK, BORROW, RETURN, REMOVE_BOOK, UPDATE_BOOK
        }
    }
}