package elibrary;

import java.time.LocalDate;
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

    public String register(String username, String password, Role role) {
        if (findUserByUsername(username) != null) {
            return "Username already exists.";
        }
        users.add(new User(username, password, role));
        return "Registered!";
    }

    public User login(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void addBook(String title, String author, int quantity) {
        int id = books.size() + 1;
        books.add(new Book(id, title, author, quantity));
    }

    public List<Book> getBooks() { return books; }
    public List<User> getUsers() { return users; }
    public List<BorrowRecord> getBorrowRecords() { return borrowRecords; }

    public User findUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public Book findBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public String borrowBook(String username, int bookId) {
        User user = findUserByUsername(username);
        if (user == null) return "User not found.";

        Book book = findBookById(bookId);
        if (book == null) return "Book not found.";
        if (book.getQuantity() <= 0) return "Book not available.";

        book.setQuantity(book.getQuantity() - 1);

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(7);

        BorrowRecord record = new BorrowRecord(user, book, borrowDate, dueDate);
        borrowRecords.add(record);

        return "Borrowed! Due in 7 days.";
    }

    public String returnBook(String username, int bookId) {
        User user = findUserByUsername(username);
        if (user == null) return "User not found.";

        for (BorrowRecord r : borrowRecords) {
            if (!r.isReturned() && r.getUser() == user && r.getBook().getId() == bookId) {
                r.setReturned(true);
                r.getBook().setQuantity(r.getBook().getQuantity() + 1);
                return "Book returned!";
            }
        }
        return "No borrowed record found.";
    }
}
