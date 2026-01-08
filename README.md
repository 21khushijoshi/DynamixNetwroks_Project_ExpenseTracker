# Expense Tracker - Java Swing Desktop Application
A simple, lightweight Expense Tracker desktop application built using Java Swing. This app allows users to track personal income and expenses, view real-time balance, display transactions in a table, and generate/download a monthly financial report.
Perfect for learning Java GUI development or for personal daily expense management without needing external databases.
Features

Add Transactions: Enter income or expenses with amount, type (Income/Expense), category, and automatic current date.
Real-time Balance: Displays current balance at the top, updated instantly after every transaction.
Green when balance is positive or zero
Red when balance is negative

Transaction Table: Clean, centered table showing all entries with Amount, Type, Category, and Date.
Monthly Report: View a summary of total income, expenses, and net balance for the current month.
Download Report: Save the monthly report as a .txt file using a file chooser dialog.
User-friendly UI: Modern-looking interface with colored headers, styled buttons, and proper spacing.

# Technologies Used

Java SE (with java.time.LocalDate)
Swing for GUI components (JFrame, JTable, JComboBox, JFileChooser, etc.)
No external libraries or databases 

# How to Run

Ensure you have JDK 8 or higher installed.
Clone or download the repository.

Compile and run the main class:
Bashjavac ExpenseTracker.java
java ExpenseTracker
Or run directly if using an IDE (IntelliJ IDEA, Eclipse, NetBeans, VS Code with Java extension).
The application will launch a window titled "Expense Tracker".

# Categories Included

Food,
Rent,
Entertainment,
Transport,
Utilities,
Other

