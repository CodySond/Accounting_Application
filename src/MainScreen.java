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

        /*

        BUTTON CLICK EVENTS HERE

        */

        // For button that reads "Input Transaction"
        inputTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Shows a pop out window that shows the message
                JOptionPane.showMessageDialog(null, "Hello");
                // TODO: (After database has been made) Transaction input screen. Type account name first, which is verified by array containing all account names from database, then amount
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
                newAccountType = newAccountType.toLowerCase();
                while(!newAccountType.equals("revenue") && !newAccountType.equals("expense") && !newAccountType.equals("asset") && !newAccountType.equals("liability")){
                    newAccountType = JOptionPane.showInputDialog(null, "Input not recognized. Input the type exactly as shown below: \nRevenue\nExpense\nAsset\nLiability");
                    newAccountType = newAccountType.toLowerCase();
                }

                // Show pop out window with name that user inputted
                JOptionPane.showMessageDialog(null, newAccount + " will be a " + newAccountType + ".");

                //TODO: Create database to store the accounts and their current values in
            }
        });
    }


    /* MAIN METHOD */
    public static void main(String[] args){
        SQLManager dataManager = new SQLManager();
        ResultSet rs;

        try {
            rs = dataManager.displayAccounts();
            while(rs.next()){
                System.out.println(rs.getString("ANAME") + " " + rs.getString("ATYPE") + " " + rs.getInt("AVALUE"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


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

