package elibrary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LibraryGUI extends JFrame {
    private Library library;
    private User currentUser;

    // Components
    private JPanel mainPanel, loginPanel, registerPanel, adminPanel, userPanel;
    private CardLayout cardLayout;

    public LibraryGUI() {
        library = new Library();
        currentUser = null;

        setTitle("E-Library");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupLoginPanel();
        setupRegisterPanel();
        setupAdminPanel();
        setupUserPanel();

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    // ------------------- Login Panel -------------------
    private void setupLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(loginBtn, gbc);
        gbc.gridx = 1;
        loginPanel.add(registerBtn, gbc);

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            currentUser = library.login(username, password);
            if (currentUser != null) {
                if (currentUser.getRole() == Role.ADMIN) {
                    refreshAdminPanel();
                    cardLayout.show(mainPanel, "admin");
                } else {
                    refreshUserPanel();
                    cardLayout.show(mainPanel, "user");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        });

        registerBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "register");
        });

        mainPanel.add(loginPanel, "login");
    }

    // ------------------- Register Panel -------------------
    private void setupRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"STUDENT", "TEACHER"};
        JComboBox<String> roleBox = new JComboBox<>(roles);

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        gbc.gridx = 0; gbc.gridy = 0;
        registerPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        registerPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        registerPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        registerPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        registerPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        registerPanel.add(roleBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        registerPanel.add(registerBtn, gbc);
        gbc.gridx = 1;
        registerPanel.add(backBtn, gbc);

        registerBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            Role role = Role.valueOf((String) roleBox.getSelectedItem());
            String msg = library.register(username, password, role);
            JOptionPane.showMessageDialog(this, msg);
            if (msg.equals("Registered!")) {
                cardLayout.show(mainPanel, "login");
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        mainPanel.add(registerPanel, "register");
    }

    // ------------------- Admin Panel -------------------
    private JTextArea adminTextArea;

    private void setupAdminPanel() {
        adminPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton addBookBtn = new JButton("Add Book");
        JButton showUsersBtn = new JButton("Show Users");
        JButton showBooksBtn = new JButton("Show Books");
        JButton logoutBtn = new JButton("Sign Out");
        topPanel.add(addBookBtn);
        topPanel.add(showBooksBtn);
        topPanel.add(showUsersBtn);
        topPanel.add(logoutBtn);

        adminTextArea = new JTextArea();
        adminTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(adminTextArea);

        adminPanel.add(topPanel, BorderLayout.NORTH);
        adminPanel.add(scrollPane, BorderLayout.CENTER);

        addBookBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Title:");
            String author = JOptionPane.showInputDialog("Author:");
            int qty = Integer.parseInt(JOptionPane.showInputDialog("Quantity:"));
            library.addBook(title, author, qty);
            refreshAdminPanel();
        });

        showBooksBtn.addActionListener(e -> refreshAdminPanel());

        showUsersBtn.addActionListener(e -> {
            List<User> users = library.getUsers();
            adminTextArea.setText("");
            for (User u : users) {
                adminTextArea.append(u.getUsername() + " (" + u.getRole() + ")\n");
            }
        });

        logoutBtn.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "login");
        });

        mainPanel.add(adminPanel, "admin");
    }

    private void refreshAdminPanel() {
        List<Book> books = library.getBooks();
        adminTextArea.setText("");
        for (Book b : books) {
            adminTextArea.append(b.getId() + ": " + b.getTitle() + " by " + b.getAuthor() + " (Qty: " + b.getQuantity() + ")\n");
        }
    }

    // ------------------- User Panel -------------------
    private JTextArea userTextArea;

    private void setupUserPanel() {
        userPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        JButton showBooksBtn = new JButton("Show Books");
        JButton borrowBtn = new JButton("Borrow");
        JButton returnBtn = new JButton("Return");
        JButton logoutBtn = new JButton("Sign Out");
        topPanel.add(showBooksBtn);
        topPanel.add(borrowBtn);
        topPanel.add(returnBtn);
        topPanel.add(logoutBtn);

        userTextArea = new JTextArea();
        userTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userTextArea);

        userPanel.add(topPanel, BorderLayout.NORTH);
        userPanel.add(scrollPane, BorderLayout.CENTER);

        showBooksBtn.addActionListener(e -> refreshUserPanel());

        borrowBtn.addActionListener(e -> {
            int bookId = Integer.parseInt(JOptionPane.showInputDialog("Book ID to borrow:"));
            String msg = library.borrowBook(currentUser.getUsername(), bookId);
            JOptionPane.showMessageDialog(this, msg);
            refreshUserPanel();
        });

        returnBtn.addActionListener(e -> {
            int bookId = Integer.parseInt(JOptionPane.showInputDialog("Book ID to return:"));
            String msg = library.returnBook(currentUser.getUsername(), bookId);
            JOptionPane.showMessageDialog(this, msg);
            refreshUserPanel();
        });

        logoutBtn.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "login");
        });

        mainPanel.add(userPanel, "user");
    }

    private void refreshUserPanel() {
        List<Book> books = library.getBooks();
        userTextArea.setText("");
        for (Book b : books) {
            userTextArea.append(b.getId() + ": " + b.getTitle() + " by " + b.getAuthor() + " (Qty: " + b.getQuantity() + ")\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryGUI::new);
    }
}

