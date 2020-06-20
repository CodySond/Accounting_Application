import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;


public class MainScreen {
    // Creates the elements and global variables
    private JButton inputTransactionButton;
    private JButton addAccountButton;

    private JPanel panelMain;

    private String newAccount;
    private String newAccountType;




    public MainScreen() {
        // TODO: Make display graphs for comparing income & expenses
        // Note: probably use JFreeChart

        /*

        BUTTON CLICK EVENTS HERE

        */

        // For button that reads "Input Transaction"
        inputTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Shows a pop out window that shows the message
                //        JOptionPane.showMessageDialog(null, "Hello");
                // TODO: (After database has been made) Transaction input screen. Type account name first, which is verified by array containing all account names from database, then amount
                // Note: Could do new window similar to this one in order to have a list of radio buttons to select account
                TransactionInput transInputObject = new TransactionInput();
                String[] transInputArgs = new String[] {"THIS IS ONLY TO CALL IT FROM ANOTHER CLASS"};
                transInputObject.main(transInputArgs);
            }
        });

        // For button that reads "Add account"
        addAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Shows pop out window to input name of new account
                newAccount = JOptionPane.showInputDialog(null, "What is the account name that you want to add?");

                // Shows new pop out to allow the user to specify the type of account
                newAccountType = JOptionPane.showInputDialog(null, "What type would you like " + newAccount + " to be? Revenue, expense, asset, or liability?");
                // Ensures that case matches what we are expecting
                newAccountType = newAccountType.toLowerCase();
                // Checks that the input account type is something we know how to handle (will bypass if so, will ask again if not)
                while(!newAccountType.equals("revenue") && !newAccountType.equals("expense") && !newAccountType.equals("asset") && !newAccountType.equals("liability")){
                    newAccountType = JOptionPane.showInputDialog(null, "Input not recognized. Input the type exactly as shown below: \nRevenue\nExpense\nAsset\nLiability");
                    newAccountType = newAccountType.toLowerCase();
                }

                // Show pop out window with name that user inputted
                JOptionPane.showMessageDialog(null, newAccount + " will be a " + newAccountType + ".");

                // Add account to database

                // Sets up dataManager object
                SQLManager dataManager = new SQLManager();

                try {
                    dataManager.addAccount(newAccount, newAccountType, 0);
                } catch (SQLException throwables) { // Catches SQLException errors
                    throwables.printStackTrace(); // Prints error to console
                } catch (ClassNotFoundException ex) { // Catches ClassNotFoundException errors
                    ex.printStackTrace(); // Prints error to console
                } finally { // Runs after everything else in the try catch structure has run
                    dataManager = null; // Closes out the dataManager object
                }
            }
        });
    }


    /* MAIN METHOD */
    public static void main(String[] args){
        // Initializing objects to display accounts in command line
        SQLManager dataManager = new SQLManager();
        ResultSet rs;

        try {
            // Sets the result set rs to be whatever displayAccounts() returns
            rs = dataManager.displayAccounts();

            // Loop until you have gone through every row in the ResultSet table
            while(rs.next()){
                // Prints the account name, type, and value for the current row
                System.out.println(rs.getString("ANAME") + " " + rs.getString("ATYPE") + " " + rs.getInt("AVALUE"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace(); // Prints error message to console
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Prints error message to console
        }

        /* DISPLAY STUFF */

        // Creates new frame to run this in
        JFrame frame = new JFrame("App");
        // Sets the frame to display the content of panelMain from the MainScreen() method above (which references the .form file
        frame.setContentPane(new MainScreen().panelMain);
        // Sets the frame to actually close when the X of the window is clicked. Default is to minimize
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        //Makes sure that the frame is visible
        frame.setVisible(true);
    }
}

