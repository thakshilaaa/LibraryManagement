package elibrary;

import java.time.LocalDate;

public class BorrowRecord {
    private String username;    // Use String instead of User
    private String bookTitle;   // Use String instead of Book
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;

    public BorrowRecord(String username, String bookTitle, LocalDate borrowDate, LocalDate dueDate) {
        this.username = username;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returned = false;
    }

    public String getUsername() {
        return username;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
