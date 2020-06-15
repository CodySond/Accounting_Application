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
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSpinner spinner3;
    private JSpinner spinner4;
    private JSpinner spinner5;
    private JSpinner spinner6;
    private JSpinner spinner7;
    private JSpinner spinner8;
    private JSpinner spinner9;
    private JSpinner spinner10;

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
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        TransactionInput dialog = new TransactionInput();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
