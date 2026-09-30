package library.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import library.book.Book;
import library.service.LibraryService;

public class Main extends JFrame {

    private LibraryService service;

    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;

    private JTable bookTable;
    private DefaultTableModel tableModel;

    public Main() {

        service = new LibraryService();

        setTitle("Library Management System");

        setSize(800, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        createGUI();

        loadBooks();
    }

    private void createGUI() {

        // -----------------------------
        // INPUT PANEL
        // -----------------------------

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        inputPanel.setBorder(
            BorderFactory.createTitledBorder("Book Details")
        );

        inputPanel.add(new JLabel("Book ID:"));

        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Title:"));

        titleField = new JTextField();
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Author:"));

        authorField = new JTextField();
        inputPanel.add(authorField);


        // -----------------------------
        // BUTTON PANEL
        // -----------------------------

        JPanel buttonPanel = new JPanel(
            new GridLayout(1, 6, 5, 5)
        );

        JButton addButton = new JButton("Add");

        JButton searchButton = new JButton("Search");

        JButton issueButton = new JButton("Issue");

        JButton returnButton = new JButton("Return");

        JButton deleteButton = new JButton("Delete");

        JButton refreshButton = new JButton("Refresh");


        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(issueButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);


        // -----------------------------
        // TABLE
        // -----------------------------

        String[] columns = {
            "Book ID",
            "Title",
            "Author",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0);

        bookTable = new JTable(tableModel);

        JScrollPane scrollPane =
            new JScrollPane(bookTable);


        // -----------------------------
        // ADD TO FRAME
        // -----------------------------

        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(inputPanel, BorderLayout.CENTER);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);


        // -----------------------------
        // BUTTON ACTIONS
        // -----------------------------

        addButton.addActionListener(e -> addBook());

        searchButton.addActionListener(e -> searchBook());

        issueButton.addActionListener(e -> issueBook());

        returnButton.addActionListener(e -> returnBook());

        deleteButton.addActionListener(e -> deleteBook());

        refreshButton.addActionListener(e -> loadBooks());
    }


    // =============================
    // ADD BOOK
    // =============================

    private void addBook() {

        try {

            int id = Integer.parseInt(
                idField.getText()
            );

            String title = titleField.getText();

            String author = authorField.getText();

            if (title.isEmpty() || author.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter title and author."
                );

                return;
            }

            String message =
                service.addBook(id, title, author);

            JOptionPane.showMessageDialog(
                this,
                message
            );

            loadBooks();

            clearFields();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Book ID must be a number."
            );
        }
    }


    // =============================
    // SEARCH BOOK
    // =============================

    private void searchBook() {

        try {

            int id = Integer.parseInt(
                idField.getText()
            );

            Book book = service.searchBook(id);

            if (book == null) {

                JOptionPane.showMessageDialog(
                    this,
                    "Book not found."
                );

            } else {

                titleField.setText(
                    book.getTitle()
                );

                authorField.setText(
                    book.getAuthor()
                );

                JOptionPane.showMessageDialog(
                    this,
                    book.toString()
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Enter a valid Book ID."
            );
        }
    }


    // =============================
    // ISSUE BOOK
    // =============================

    private void issueBook() {

        try {

            int id = Integer.parseInt(
                idField.getText()
            );

            String message =
                service.issueBook(id);

            JOptionPane.showMessageDialog(
                this,
                message
            );

            loadBooks();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Enter a valid Book ID."
            );
        }
    }


    // =============================
    // RETURN BOOK
    // =============================

    private void returnBook() {

        try {

            int id = Integer.parseInt(
                idField.getText()
            );

            String message =
                service.returnBook(id);

            JOptionPane.showMessageDialog(
                this,
                message
            );

            loadBooks();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Enter a valid Book ID."
            );
        }
    }


    // =============================
    // DELETE BOOK
    // =============================

    private void deleteBook() {

        try {

            int id = Integer.parseInt(
                idField.getText()
            );

            String message =
                service.deleteBook(id);

            JOptionPane.showMessageDialog(
                this,
                message
            );

            loadBooks();

            clearFields();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Enter a valid Book ID."
            );
        }
    }


    // =============================
    // LOAD BOOKS INTO TABLE
    // =============================

    private void loadBooks() {

        tableModel.setRowCount(0);

        List<Book> books =
            service.getAllBooks();

        for (Book book : books) {

            Object[] row = {

                book.getBookId(),

                book.getTitle(),

                book.getAuthor(),

                book.isAvailable()
                    ? "Available"
                    : "Issued"
            };

            tableModel.addRow(row);
        }
    }


    // =============================
    // CLEAR FIELDS
    // =============================

    private void clearFields() {

        idField.setText("");

        titleField.setText("");

        authorField.setText("");
    }


    // =============================
    // MAIN
    // =============================

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            Main gui = new Main();

            gui.setVisible(true);
        });
    }
}