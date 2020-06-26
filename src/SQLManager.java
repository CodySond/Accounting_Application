import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.*;
import java.util.Arrays;

// TODO: Either add in error correction to correct floating point errors with the stored value of accounts or change the value to represent cents (only 3 countries use thousandths of currency)
// Re: above note: possibly not necessary, as it seems that floating point is accurate for addition and subtraction with the two decimal places that this uses.

public class SQLManager {

    //Database stuff
    private static Connection con;
    private static boolean hasData = false;

    /* DATABASE FUNCTIONS */

    /**
     * Retrieves all the content from the "accounts" table, including the name, type, and value of each account
     *
     * @return ResultSet containing the content of table "accounts"
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ResultSet displayAccounts() throws SQLException, ClassNotFoundException {
        if(con == null || con.isClosed()) {
            getConnection();
        }

        //ResultSet res = null;
        //Statement state = null;
        //try {

        Statement state = con.createStatement(); // Creates statement to be used
        ResultSet res = state.executeQuery("SELECT * FROM accounts"); // Selects all data from table "accounts"
        //} finally {
            //try {state.close();} catch (SQLException e) { }
            //try {con.close();} catch (SQLException e) { }
        //}
        return res;

    }

    /**
     * Establishes connection to database for object "con", which is an object of the parent method/class
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private void getConnection() throws SQLException, ClassNotFoundException { // Shouldn't close connection, as that would make this not work
        // Tells Java which driver to use
        Class.forName("org.h2.Driver");
        // Establishes connection, string in the parentheses is where the database is according to the driver
        con = DriverManager.getConnection("jdbc:h2:~/AccountingApp.db");
        // Run the database initialization code
        initialize();
    }

    /**
     * Does initial checks to see if the required tables exist in the database; is called by getConnection() method
     * @throws SQLException
     */
    private void initialize() throws SQLException { // Shouldn't close connection because it is part of getConnection() method
        if (!hasData){
            hasData = true;

            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='TABLE' AND TABLE_NAME='ACCOUNTS'"); // NOTE: NOT SURE IF THIS ACTUALLY WORKS OR NOT
            // Close statement "state"
            if(!res.next()){
                // Console output for debugging
                System.out.println("Building sample database");

                /* Build table */

                // Create statement
                Statement state2 = null;
                try {
                    // Link statement to connection
                    state2 = con.createStatement();

                    // Tell statement what instructions it should have and to execute those
                    state2.execute("CREATE TABLE IF NOT EXISTS accounts(id bigint auto_increment,"
                                    + "aname VARCHAR(60)," + "atype VARCHAR(60)," + "avalue INT);"
                            /*+ "primary key(id));"*/);
                    // The above statement creates a table with four columns: an id column that automatically increments,
                    // aname to store the name of each account, atype to store the type of each account (revenue, expense, etc.),
                    // and avalue to store each accounts value

                } finally {
                    // Closes out the statement
                    try{ state2.close(); } catch (SQLException e) { }
                }



                /* Inserting default data */

                // Create statement object
                PreparedStatement prep = null;
                try {
                    // Give the statement what it is supposed to do
                    prep = con.prepareStatement("INSERT INTO accounts(aname,atype,avalue) VALUES(?,?,?);");
                    //prep.execute();
                    ///*

                    // Set the value of the question marks in the parentheses after "VALUES" above
                    prep.setString(1, "Revenue");
                    prep.setString(2, "revenue");
                    prep.setInt(3, 0);

                    // Tells computer to execute the statement and commit it to the database
                    prep.execute();
                } finally {
                    // Closes the statement, freeing up the memory space
                    try {prep.close();} catch (SQLException e) { }
                }

                //*/
            }
            state.close();

            // Creating transaction database
            Statement stateTrans = con.createStatement();
            ResultSet resTrans = stateTrans.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='TABLE' AND TABLE_NAME='TRANSACTIONS_JOURNAL'");

            if(!resTrans.next()){
                System.out.println("Building journal database");

                // Build table
                Statement state3 = null;
                try{
                    state3 = con.createStatement();
                    state3.execute("CREATE TABLE IF NOT EXISTS transactions_journal(id bigint auto_increment,"
                    + "dacct1 VARCHAR(60)," + "dval1 DECIMAL(8,2)," + "dacct2 VARCHAR(60)," + "dval2 DECIMAL(8,2),"
                    + "dacct3 VARCHAR(60)," + "dval3 DECIMAL(8,2)," + "dacct4 VARCHAR(60)," + "dval4 DECIMAL(8,2),"
                    + "dacct5 VARCHAR(60)," + "dval5 DECIMAL(8,2)," + "cacct1 VARCHAR(60)," + "cval1 DECIMAL(8,2),"
                    + "cacct2 VARCHAR(60)," + "cval2 DECIMAL(8,2)," + "cacct3 VARCHAR(60)," + "cval3 DECIMAL(8,2),"
                    + "cacct4 VARCHAR(60)," + "cval4 DECIMAL(8,2)," + "cacct5 VARCHAR(60)," + "cval5 DECIMAL(8,2),"
                    + "descr MEDIUMTEXT," + "transdate DATE," + "transtime TIME);");
                } finally {
                    try { state3.close(); } catch (SQLException ex) { }
                }
                System.out.println("Journal database created");
            }

            stateTrans.close();


            Statement stateTransSimple = con.createStatement();
            ResultSet resTransSimple = stateTransSimple.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='TABLE' AND TABLE_NAME='TRANS_JOURNAL_SIMPLE'");

            if(!resTransSimple.next()){
                System.out.println("Building simple journal database");

                // Build table
                Statement state4 = null;
                try{
                    state4 = con.createStatement();
                    state4.execute("CREATE TABLE IF NOT EXISTS trans_journal_simple(id bigint auto_increment,"
                    + "acct1 VARCHAR(60)," + "val1 DECIMAL(8,2)," + "acct2 VARCHAR(60)," + "val2 DECIMAL(8,2),"
                    + "acct3 VARCHAR(60)," + "val3 DECIMAL(8,2)," + "acct4 VARCHAR(60)," + "val4 DECIMAL(8,2),"
                    + "acct5 VARCHAR(60)," + "val5 DECIMAL(8,2)," + "acct6 VARCHAR(60)," + "val6 DECIMAL(8,2),"
                    + "acct7 VARCHAR(60)," + "val7 DECIMAL(8,2)," + "acct8 VARCHAR(60)," + "val8 DECIMAL(8,2),"
                    + "acct9 VARCHAR(60)," + "val9 DECIMAL(8,2)," + "acct10 VARCHAR(60)," + "val10 DECIMAL(8,2),"
                    + "descr MEDIUMTEXT," + "transdate DATE," + "transtime TIME);");
                } finally {
                    try { state4.close(); } catch(SQLException e) { }
                }
            }

            stateTransSimple.close();
        }

        // Debugging console output
        System.out.println("Database exists. Initialization complete");
    }

    // "addAccount(...)" is used to add new accounts to the database

    /**
     * Adds a new account to the "accounts" table of the database
     *
     * @param accountName Name of the new account (case-sensitive)
     * @param accountType What kind of account this account is (options: asset, revenue, expense, liability)
     * @param accountValue The initial value of the new account; should usually be 0
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void addAccount(String accountName, String accountType, int accountValue) throws SQLException, ClassNotFoundException {
        // Checks for database connection. If it doesn't exist, it connects to database
        if(con == null){
            getConnection();
        }

        // Creating statement object such that it can be closed in the "finally" block
        PreparedStatement prep = null;
        try {
            // Statement to add data into table
            prep = con.prepareStatement("INSERT INTO accounts(aname,atype,avalue) VALUES(?,?,?);"); // Values in parentheses after "accounts" are the column names,
            // question marks in parentheses afters "VALUES" show that we are going to pass in the values for the columns later on

            // Set the question marks to be the proper values
            prep.setString(1, accountName);
            prep.setString(2, accountType);
            prep.setInt(3, accountValue);

            // Execute statement and commit changes to database
            prep.execute();
        } finally {
            try { prep.close(); } catch (SQLException e) { } // Closes prepared statement used to insert data
            try { con.close(); } catch (SQLException e) { } // Closes connection

        }

    }

    /**
     * Adds a new transaction to the database and updates the "AVALUE" column of "accounts" table
     * Order of accounts in parameters doesn't matter so long as they are in the right section (debit/credit)
     *
     * @param debAcct1 Account to be debited
     * @param debAmt1 Amount to be debited to account of matching number
     * @param debAcct2 Account to be debited
     * @param debAmt2 Amount to be debited to account of matching number
     * @param debAcct3 Account to be debited
     * @param debAmt3 Amount to be debited to account of matching number
     * @param debAcct4 Account to be debited
     * @param debAmt4 Amount to be debited to account of matching number
     * @param debAcct5 Account to be debited
     * @param debAmt5 Amount to be debited to account of matching number
     * @param credAcct1 Account to be credited
     * @param credAmt1 Account to be credited to account of matching number
     * @param credAcct2 Account to be credited
     * @param credAmt2 Account to be credited to account of matching number
     * @param credAcct3 Account to be credited
     * @param credAmt3 Account to be credited to account of matching number
     * @param credAcct4 Account to be credited
     * @param credAmt4 Account to be credited to account of matching number
     * @param credAcct5 Account to be credited
     * @param credAmt5 Account to be credited to account of matching number
     * @param descr Description of the transaction
     */
    public void addTransaction(String debAcct1, double debAmt1, String debAcct2, double debAmt2, String debAcct3, double debAmt3, String debAcct4, double debAmt4,
                               String debAcct5, double debAmt5, String credAcct1, double credAmt1, String credAcct2, double credAmt2, String credAcct3, double credAmt3,
                               String credAcct4, double credAmt4, String credAcct5, double credAmt5, String descr){
        try {
            if(con == null) {
                getConnection();
            }
        } catch(SQLException e) { } catch(ClassNotFoundException e) { }

        PreparedStatement prep = null;
        PreparedStatement prepSimpleJournal = null;
        try {
            // Create statement
            prep = con.prepareStatement("INSERT INTO TRANSACTIONS_JOURNAL(dacct1,dval1,dacct2,dval2,dacct3,dval3,dacct4,dval4,dacct5,dval5,cacct1,cval1,cacct2,cval2," +
                    "cacct3,cval3,cacct4,cval4,cacct5,cval5,descr,transdate,transtime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");

            // Fill values into statement
                //Debit section
            prep.setString(1, debAcct1);
            prep.setDouble(2, debAmt1);
            prep.setString(3, debAcct2);
            prep.setDouble(4, debAmt2);
            prep.setString(5, debAcct3);
            prep.setDouble(6, debAmt3);
            prep.setString(7, debAcct4);
            prep.setDouble(8, debAmt4);
            prep.setString(9, debAcct5);
            prep.setDouble(10, debAmt5);

                //Credit section
            prep.setString(11, credAcct1);
            prep.setDouble(12, credAmt1);
            prep.setString(13, credAcct2);
            prep.setDouble(14, credAmt2);
            prep.setString(15, credAcct3);
            prep.setDouble(16, credAmt3);
            prep.setString(17, credAcct4);
            prep.setDouble(18, credAmt4);
            prep.setString(19, credAcct5);
            prep.setDouble(20, credAmt5);

                //Other section
            prep.setString(21, descr);

            LocalDate CurrentDate = LocalDate.now(); // Get the current date, supposedly supported by H2
            prep.setObject(22, CurrentDate);

            LocalTime CurrentTime = LocalTime.now(); // Get current time
            prep.setObject(23, CurrentTime);

            prep.execute();

            CurrentDate = null;
            CurrentTime = null;

            /* Simplified journal & accounts update */

            // Setting up objects for section
            ResultSet AccountsSet = null;
            String[] transAccountNames = {debAcct1,debAcct2,debAcct3,debAcct4,debAcct5,credAcct1,credAcct2,credAcct3,credAcct4,credAcct5};
            double[] transValues = {debAmt1,debAmt2,debAmt3,debAmt4,debAmt5,credAmt1,credAmt2,credAmt3,credAmt4,credAmt5};

            // Figure out what accounts exist
            try{ AccountsSet = displayAccounts(); } catch (SQLException e) { } catch (ClassNotFoundException e) { }
            if(AccountsSet != null) { // If the try block above succeeded
                while (AccountsSet.next()){ // Loop through all the accounts in the accounts set
                    String CurrentAccountSelected = AccountsSet.getString("ANAME"); // Get name of currently selected account
                    String CurrentAccountType = AccountsSet.getString("ATYPE"); // Get type of currently selected account

                    // Put variables into an array, such that can iterate through in for loop
                    for (int i = 0; i<10; i++) {
                        if(CurrentAccountSelected.equals(transAccountNames[i])){ // Check if the account from the set is one of the transaction accounts
                            if(CurrentAccountType.equals("asset") || CurrentAccountType.equals("expense")){ // Debit side accounts
                                // Add debAmt1 to AVALUE
                                // To add, retrieve integer from table ("AccountsSet.getInt("AVALUE")") and store as variable
                                // Add debAmt1 to that value from table as variable newvalue
                                // Update value in table ("UPDATE ACCOUNTS SET AVALUE=" + newvalue + " WHERE ANAME =" + CurrentAccountSelected)
                                // Put this into the the simplified journal as well (just record account name, change (+/-), date, and time
                                // Use transaction id maybe? To ease troubleshooting and connect the 2 tables more clearly

                                double newValue;
                                Statement stateTransAndAccountUpdate = con.createStatement();
                                if(i<5){ // Check if it is in the debit side
                                    double currentValue = AccountsSet.getDouble("AVALUE");
                                    newValue = currentValue + transValues[i];
                                    try {
                                        stateTransAndAccountUpdate.execute("UPDATE ACCOUNTS SET AVALUE=" + newValue + " WHERE ANAME='" + CurrentAccountSelected + "';");
                                    } finally {
                                        stateTransAndAccountUpdate.close();
                                    }
                                } else { // If it is on the credit side
                                    double currentValue = AccountsSet.getDouble("AVALUE");
                                    newValue = currentValue - transValues[i];
                                    try {
                                        stateTransAndAccountUpdate.execute("UPDATE ACCOUNTS SET AVALUE=" + newValue + " WHERE ANAME='" + CurrentAccountSelected + "';");
                                    } finally {
                                        stateTransAndAccountUpdate.close();
                                    }
                                }

                            } else if(CurrentAccountType.equals("revenue") || CurrentAccountType.equals("liability")){ // Credit side accounts
                                // Subtract debAmt1 from AVALUE
                                // See method set out above

                                double newValue;
                                Statement stateTransAndAccountUpdate = con.createStatement();
                                if(i>=5){ // Check if it is in the credit side
                                    double currentValue = AccountsSet.getDouble("AVALUE");
                                    newValue = currentValue + transValues[i];
                                    try {
                                        stateTransAndAccountUpdate.execute("UPDATE ACCOUNTS SET AVALUE=" + newValue + " WHERE ANAME='" + CurrentAccountSelected + "';");
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    } finally {
                                        stateTransAndAccountUpdate.close();
                                    }
                                } else { // If it is on the debit side
                                    double currentValue = AccountsSet.getDouble("AVALUE");
                                    newValue = currentValue - transValues[i];
                                    try {
                                        stateTransAndAccountUpdate.execute("UPDATE ACCOUNTS SET AVALUE=" + newValue + " WHERE ANAME='" + CurrentAccountSelected + "';");
                                    } finally {
                                        stateTransAndAccountUpdate.close();
                                    }
                                }
                            } else {
                                System.out.println("Error: No debit or credit classification found");
                            }
                        }
                    }
                }
            }

        } catch(SQLException e) { } finally {
            try{ prep.close(); } catch(SQLException e) { } catch(NullPointerException e) { }
            try{ con.close(); } catch(SQLException e) { }
        }

    }


    public void deleteAllTables(){
        if(con == null){
            try {
                getConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Statement accountsStatement = null;
        Statement transStatement = null;
        Statement simpleTransStatement = null;
        try{
            accountsStatement = con.createStatement();
            accountsStatement.execute("DROP TABLE ACCOUNTS;");

            transStatement = con.createStatement();
            transStatement.execute("DROP TABLE TRANSACTIONS_JOURNAL;");

            simpleTransStatement = con.createStatement();
            simpleTransStatement.execute("DROP TABLE TRANS_JOURNAL_SIMPLE;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {

            hasData = false;

            // Recreate tables
            try { initialize(); } catch (SQLException throwables) { throwables.printStackTrace(); }

            // Close statements & connection
            try {
                accountsStatement.close();
                transStatement.close();
                simpleTransStatement.close();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    // TODO: IMPORTANT: FIND WAY TO CLOSE CONNECTION AND STATEMENTS!
    // Mostly done, only need to figure out way to do it for returning ResultSet for displayAccounts()
    // Could have the connection created in a larger object

}
