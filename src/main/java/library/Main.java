public class Main {

    public static void main(String[] args) {

        Library library = new Library();

        // Добавление книг
        library.addBook(new Book(1, "Война и мир",
                "Л.Н. Толстой", 1869, "978-5-17-090335-2"));

        library.addBook(new Book(2, "Преступление и наказание",
                "Ф.М. Достоевский", 1866, "978-5-17-090336-9"));

        library.addBook(new Book(3, "Анна Каренина",
                "Л.Н. Толстой", 1877, "978-5-17-090337-6"));

        // Поиск книги
        System.out.println(library.findBookById(1));

        // Поиск по автору
        System.out.println("\nКниги Л.Н. Толстого:");
        for (Book book : library.findBooksByAuthor("Л.Н. Толстой")) {
            System.out.println(book);
        }

        // Выдача и возврат
        library.borrowBook(1);
        library.returnBook(1);

        // Доступные книги
        System.out.println("\nДоступные книги:");
        for (Book book : library.getAvailableBooks()) {
            System.out.println(book);
        }

        // Журнал операций
        System.out.println();
        library.printOperationLog();
    }
}