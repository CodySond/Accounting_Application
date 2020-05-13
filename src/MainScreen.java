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

    //Database stuff
    private static Connection con;
    private static boolean hasData = false;


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

    /* DATABASE FUNCTIONS */
    public ResultSet displayAccounts() throws SQLException, ClassNotFoundException {
        if(con == null) {
            getConnection();
        }

        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT aname FROM accounts");
        return res;
    }

    private void getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        con = DriverManager.getConnection("jdbc:h2:~/AccountingApp.db");
        initialize();
    }

    private void initialize() throws SQLException {
        if (!hasData){
            hasData = true;

            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT name FROM AccountingApp.TABLES WHERE type='table' AND name='accounts'"); // NOTE: NOT SURE IF THIS ACTUALLY WORKS OR NOT
            if(!res.next()){

                //Build table
                Statement state2 = con.createStatement();
                state2.execute("CREATE TABLE accounts(id integer,"
                + "aname varchar(60)," + "atype varchar(60)," + "avalue integer,"
                + "primary key(id));");

                //Insert default data
                PreparedStatement prep = con.prepareStatement("INSERT INFO accounts(?,?,?,?);");
                prep.setString(2, "");
            }
        }
    }


    /* MAIN METHOD */
    public static void main(String[] args){
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

