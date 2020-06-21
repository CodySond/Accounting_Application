import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.event.*;
import java.sql.*;

public class TransactionInput extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel accountSelectorPane;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JComboBox comboBox7;
    private JComboBox comboBox8;
    private JComboBox comboBox9;
    private JComboBox comboBox10;
    private JPanel transactionValueInputPane;
    private JTextArea textArea1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JTextField textField10;

    public TransactionInput() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        textField1.setText("0");
        textField2.setText("0");
        textField3.setText("0");
        textField4.setText("0");
        textField5.setText("0");
        textField6.setText("0");
        textField7.setText("0");
        textField8.setText("0");
        textField9.setText("0");
        textField10.setText("0");

        // Setup combo boxes (drop-down menus)
        comboBox1.addItem(" ");
        comboBox2.addItem(" ");
        comboBox3.addItem(" ");
        comboBox4.addItem(" ");
        comboBox5.addItem(" ");
        comboBox6.addItem(" ");
        comboBox7.addItem(" ");
        comboBox8.addItem(" ");
        comboBox9.addItem(" ");
        comboBox10.addItem(" ");

        SQLManager dataManager;
        ResultSet accountsSet;
        try {
            dataManager = new SQLManager();
            accountsSet = dataManager.displayAccounts();
            while(accountsSet.next()){
                comboBox1.addItem(accountsSet.getString("ANAME"));
                comboBox2.addItem(accountsSet.getString("ANAME"));
                comboBox3.addItem(accountsSet.getString("ANAME"));
                comboBox4.addItem(accountsSet.getString("ANAME"));
                comboBox5.addItem(accountsSet.getString("ANAME"));
                comboBox6.addItem(accountsSet.getString("ANAME"));
                comboBox7.addItem(accountsSet.getString("ANAME"));
                comboBox8.addItem(accountsSet.getString("ANAME"));
                comboBox9.addItem(accountsSet.getString("ANAME"));
                comboBox10.addItem(accountsSet.getString("ANAME"));
                System.out.println("Added account " + accountsSet.getString("ANAME") + " to all combo boxes.");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            dataManager = null;
            accountsSet = null;
        }
    }

    private void onOK() {
        SQLManager dataManager;
        boolean onlyAcceptedCharacters = true;

        String debAcct1 = " ";
        String debAcct2 = " ";
        String debAcct3 = " ";
        String debAcct4 = " ";
        String debAcct5 = " ";
        String credAcct1 = " ";
        String credAcct2 = " ";
        String credAcct3 = " ";
        String credAcct4 = " ";
        String credAcct5 = " ";

        double debAmt1 = 0;
        double debAmt2 = 0;
        double debAmt3 = 0;
        double debAmt4 = 0;
        double debAmt5 = 0;
        double credAmt1 = 0;
        double credAmt2 = 0;
        double credAmt3 = 0;
        double credAmt4 = 0;
        double credAmt5 = 0;

        String descrip = " ";

        try {
            dataManager = new SQLManager();

            descrip = String.valueOf(textArea1.getText());

            debAcct1 = String.valueOf(comboBox1.getSelectedItem());
            debAcct2 = String.valueOf(comboBox2.getSelectedItem());
            debAcct3 = String.valueOf(comboBox3.getSelectedItem());
            debAcct4 = String.valueOf(comboBox4.getSelectedItem());
            debAcct5 = String.valueOf(comboBox5.getSelectedItem());
            credAcct1 = String.valueOf(comboBox6.getSelectedItem());
            credAcct2 = String.valueOf(comboBox7.getSelectedItem());
            credAcct3 = String.valueOf(comboBox8.getSelectedItem());
            credAcct4 = String.valueOf(comboBox9.getSelectedItem());
            credAcct5 = String.valueOf(comboBox10.getSelectedItem());

            System.out.println(debAcct1);
            System.out.println(debAcct2);
            System.out.println(debAcct3);
            System.out.println(debAcct4);
            System.out.println(debAcct5);
            System.out.println(credAcct1);
            System.out.println(credAcct2);
            System.out.println(credAcct3);
            System.out.println(credAcct4);
            System.out.println(credAcct5);

            String debAmt1String = String.valueOf(textField1.getText());
            String debAmt2String = String.valueOf(textField2.getText());
            String debAmt3String = String.valueOf(textField3.getText());
            String debAmt4String = String.valueOf(textField4.getText());
            String debAmt5String = String.valueOf(textField5.getText());
            String credAmt1String = String.valueOf(textField6.getText());
            String credAmt2String = String.valueOf(textField7.getText());
            String credAmt3String = String.valueOf(textField8.getText());
            String credAmt4String = String.valueOf(textField9.getText());
            String credAmt5String = String.valueOf(textField10.getText());

            /*
            if(debAmt1String.matches("^-?\\d+\\.?\\d*$")){
                System.out.println("Text box only contains numbers");
                onlyAcceptedCharacters = true;
            } else {
                System.out.println("Text box contains unaccepted characters");
            }
            */

            try{
                debAmt1 = Double.parseDouble(debAmt1String);
                debAmt2 = Double.parseDouble(debAmt2String);
                debAmt3 = Double.parseDouble(debAmt3String);
                debAmt4 = Double.parseDouble(debAmt4String);
                debAmt5 = Double.parseDouble(debAmt5String);
                credAmt1 = Double.parseDouble(credAmt1String);
                credAmt2 = Double.parseDouble(credAmt2String);
                credAmt3 = Double.parseDouble(credAmt3String);
                credAmt4 = Double.parseDouble(credAmt4String);
                credAmt5 = Double.parseDouble(credAmt5String);
            } catch(NullPointerException nullPointer){
                nullPointer.printStackTrace();
                onlyAcceptedCharacters = false;
            } catch(NumberFormatException numberFormatException){
                numberFormatException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Unacceptable characters in transaction value box(es). Only digits and decimal points are accepted.");
                onlyAcceptedCharacters = false;
            }
        } finally {
            dataManager = null;
        }

        /* CLOSING OUT OF WINDOW */
        SQLManager dataManagerCommit;
        if(onlyAcceptedCharacters) {
            try {
                dataManagerCommit = new SQLManager();
                dataManagerCommit.addTransaction(debAcct1, debAmt1, debAcct2, debAmt2, debAcct3, debAmt3, debAcct4, debAmt4, debAcct5, debAmt5, credAcct1, credAmt1, credAcct2, credAmt2,
                        credAcct3, credAmt3, credAcct4, credAmt4, credAcct5, credAmt5, descrip);
            } finally {
                dataManagerCommit = null;
            }
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        TransactionInput dialog = new TransactionInput();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0); <- This is what was causing both windows to close when ok or cancel were clicked
    }
}
