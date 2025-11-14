package elibrary;

public class LibraryApp {
    public static void main(String[] args) {
        // Create and show the GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LibraryGUI();
        });
    }
}
