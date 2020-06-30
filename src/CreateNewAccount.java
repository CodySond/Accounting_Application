import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateNewAccount extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JRadioButton revenueRadioButton;
    private JRadioButton expenseRadioButton;
    private JRadioButton assetRadioButton;
    private JRadioButton liabilityRadioButton;
    private JRadioButton ownerSEquityRadioButton;
    private JRadioButton ownerSWithdrawalRadioButton;

    public CreateNewAccount() {
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

        ButtonGroup radioButtonsGroup = new ButtonGroup();
        radioButtonsGroup.add(revenueRadioButton);
        radioButtonsGroup.add(expenseRadioButton);
        radioButtonsGroup.add(assetRadioButton);
        radioButtonsGroup.add(liabilityRadioButton);
        radioButtonsGroup.add(ownerSEquityRadioButton);
        radioButtonsGroup.add(ownerSWithdrawalRadioButton);
    }

    private void onOK() {
        Boolean acceptableToPassToDatabase = true;
        String newAccountName = textField1.getText();
        if(newAccountName.length() > 60){
            acceptableToPassToDatabase = false;
            JOptionPane.showMessageDialog(null, "Account name too long. Please shorten it.");
        }

        String newAccountType = findSelectedTypeString();
        if(newAccountType.equals("error")){
            acceptableToPassToDatabase = false;
        }

        SQLManager dataInput = new SQLManager();
        ResultSet rs;
        try {
            rs = dataInput.displayAccounts();
            while(rs.next()){
                if(newAccountName.equals(rs.getString("ANAME"))){
                    acceptableToPassToDatabase = false;
                }
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(acceptableToPassToDatabase){
            try {
                dataInput.addAccount(newAccountName, newAccountType, 0);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            dataInput = null;
            dispose();
        }
        //dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private String findSelectedTypeString(){
        if(revenueRadioButton.isSelected()){
            return "revenue";
        } else if(expenseRadioButton.isSelected()){
            return "expense";
        } else if(assetRadioButton.isSelected()){
            return "asset";
        } else if(liabilityRadioButton.isSelected()){
            return "liability";
        } else if(ownerSEquityRadioButton.isSelected()){
            return "ownerequity";
        } else if(ownerSWithdrawalRadioButton.isSelected()){
            return "ownerwithdrawal";
        } else {
            JOptionPane.showMessageDialog(null, "No account type selected. Please select an account type from the list.");
        }
        return "error";
    }

    public static void main(String[] args) {
        CreateNewAccount dialog = new CreateNewAccount();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
    }
}
