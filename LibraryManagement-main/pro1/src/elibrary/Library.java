package elibrary;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<User> users;
    private List<Book> books;
    private List<BorrowRecord> borrowRecords;

    public Library() {
        users = new ArrayList<>();
        books = new ArrayList<>();
        borrowRecords = new ArrayList<>();

        // Default admin
        users.add(new User("admin", "admin", Role.ADMIN));
    }

    // -------------------- USER FUNCTIONS --------------------

    public String register(String username, String password, Role role) {
        if (findUserByUsername(username) != null) {
            return "Username already exists!";
        }
        users.add(new User(username, password, role));
        return "Registered!";
    }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return users;
    }

    // Helper
    private User findUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    // -------------------- BOOK FUNCTIONS --------------------

    public void addBook(String title, String author, int quantity) {
        int id = books.size() + 1;
        books.add(new Book(id, title, author, quantity));
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book findBook(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }


    // -------------------- BORROW FUNCTIONS --------------------

    public String borrowBook(String username, int bookId) {
        User user = findUserByUsername(username);
        if (user == null) return "User not found.";

        Book book = findBook(bookId);
        if (book == null) return "Book not found.";
        if (book.getQuantity() <= 0) return "Book not available.";

        // reduce quantity
        book.setQuantity(book.getQuantity() - 1);

        // create record
        BorrowRecord record = new BorrowRecord(
                username,
                book.getTitle(),
                java.time.LocalDate.now(),
                java.time.LocalDate.now().plusDays(14)
        );

        borrowRecords.add(record);

        return "Book borrowed!";
    }

    public String returnBook(String username, int bookId) {

        Book book = findBook(bookId);
        if (book == null) return "Book not found.";

        for (BorrowRecord r : borrowRecords) {
            if (!r.isReturned()
                    && r.getUsername().equals(username)
                    && r.getBookTitle().equals(book.getTitle())) {

                r.setReturned(true);
                book.setQuantity(book.getQuantity() + 1);
                return "Book returned!";
            }
        }

        return "No borrowed record found.";
    }
}
