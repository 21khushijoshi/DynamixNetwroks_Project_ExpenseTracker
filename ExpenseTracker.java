import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Simple Expense Tracker using Java Swing
 * Allows user to add income/expense, view balance,
 * generate monthly report and download it as a text file.
 */
public class ExpenseTracker extends JFrame {

    // Stores all transactions entered by user
    private ArrayList<Transaction> transactionList = new ArrayList<>();

    // Table model for displaying data
    private DefaultTableModel tableModel;

    // Label to show current balance
    private JLabel balanceLabel;

    public ExpenseTracker() {

        setTitle("Expense Tracker");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        //Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBackground(new Color(0, 128, 128));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        balanceLabel = new JLabel("Balance: ₹0.0");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        balanceLabel.setForeground(Color.WHITE);
        topPanel.add(balanceLabel);

        add(topPanel, BorderLayout.NORTH);

        //Table Section 
        tableModel = new DefaultTableModel(
                new String[]{"Amount", "Type", "Category", "Date"}, 0
        );

        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 128, 128));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        // Center align table data
        DefaultTableCellRenderer centerAlign = new DefaultTableCellRenderer();
        centerAlign.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerAlign);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);

        //Bottom Input Panel
        JPanel bottomPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        bottomPanel.setBackground(new Color(240, 244, 247));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField amountField = new JTextField();

        JComboBox<String> typeCombo =
                new JComboBox<>(new String[]{"Income", "Expense"});

        JComboBox<String> categoryCombo =
                new JComboBox<>(new String[]{
                        "Food", "Rent", "Entertainment",
                        "Transport", "Utilities", "Other"
                });

        JButton addButton = new JButton("Add Transaction");
        JButton reportButton = new JButton("Monthly Report");
        JButton downloadButton = new JButton("Download Report");

        styleButton(addButton);
        styleButton(reportButton);
        styleButton(downloadButton);

        bottomPanel.add(new JLabel("Amount:"));
        bottomPanel.add(new JLabel("Type:"));
        bottomPanel.add(new JLabel("Category:"));
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(new JLabel(""));

        bottomPanel.add(amountField);
        bottomPanel.add(typeCombo);
        bottomPanel.add(categoryCombo);
        bottomPanel.add(addButton);
        bottomPanel.add(reportButton);
        bottomPanel.add(downloadButton);

        add(bottomPanel, BorderLayout.SOUTH);

        //Button Actions 
        addButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String type = typeCombo.getSelectedItem().toString();
                String category = categoryCombo.getSelectedItem().toString();

                Transaction tx = new Transaction(
                        amount, type, category, LocalDate.now()
                );

                transactionList.add(tx);
                tableModel.addRow(
                        new Object[]{amount, type, category, tx.date}
                );

                amountField.setText("");
                updateBalance();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this, "Please enter a valid numeric amount"
                );
            }
        });

        reportButton.addActionListener(e -> showMonthlyReport());
        downloadButton.addActionListener(e -> saveMonthlyReport());

        setVisible(true);
    }

    // Balance Calculation 
    private void updateBalance() {
        double income = 0;
        double expense = 0;

        for (Transaction t : transactionList) {
            if (t.type.equals("Income")) {
                income += t.amount;
            } else {
                expense += t.amount;
            }
        }

        double balance = income - expense;
        balanceLabel.setText("Balance: ₹" + balance);

        if (balance >= 0) {
            balanceLabel.setForeground(new Color(0, 200, 83));
        } else {
            balanceLabel.setForeground(Color.RED);
        }
    }

    //Monthly Report 
    private void showMonthlyReport() {
        double[] values = calculateMonthlyTotals();

        JOptionPane.showMessageDialog(this,
                "Monthly Report\n" +
                "Income: ₹" + values[0] +
                "\nExpenses: ₹" + values[1] +
                "\nBalance: ₹" + (values[0] - values[1])
        );
    }

    private void saveMonthlyReport() {
        double[] values = calculateMonthlyTotals();

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(
                new java.io.File("Monthly_Report.txt")
        );

        if (chooser.showSaveDialog(this)
                == JFileChooser.APPROVE_OPTION) {

            try (FileWriter writer =
                         new FileWriter(chooser.getSelectedFile())) {

                writer.write("MONTHLY EXPENSE REPORT\n");
                writer.write("----------------------\n");
                writer.write("Income: ₹" + values[0] + "\n");
                writer.write("Expenses: ₹" + values[1] + "\n");
                writer.write("Balance: ₹" + (values[0] - values[1]) + "\n");

                JOptionPane.showMessageDialog(
                        this, "Report saved successfully"
                );

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        this, "Error while saving file"
                );
            }
        }
    }

    private double[] calculateMonthlyTotals() {
        double income = 0;
        double expense = 0;

        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        for (Transaction t : transactionList) {
            if (t.date.getMonthValue() == currentMonth
                    && t.date.getYear() == currentYear) {

                if (t.type.equals("Income")) {
                    income += t.amount;
                } else {
                    expense += t.amount;
                }
            }
        }
        return new double[]{income, expense};
    }

    //Button Styling 
    private void styleButton(JButton button) {
        button.setBackground(new Color(184, 160, 66));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseTracker());
    }
}

// Transaction Model 
class Transaction {
    double amount;
    String type;
    String category;
    LocalDate date;

    Transaction(double amount, String type,
                String category, LocalDate date) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }
}


