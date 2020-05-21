import java.sql.*;

public class SQLManager {

    //Database stuff
    private static Connection con;
    private static boolean hasData = false;

    /* DATABASE FUNCTIONS */
    public ResultSet displayAccounts() throws SQLException, ClassNotFoundException {
        if(con == null) {
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

    private void getConnection() throws SQLException, ClassNotFoundException { // Shouldn't close connection, as that would make this not work
        // Tells Java which driver to use
        Class.forName("org.h2.Driver");
        // Establishes connection, string in the parentheses is where the database is according to the driver
        con = DriverManager.getConnection("jdbc:h2:~/AccountingApp.db");
        // Run the database initialization code
        initialize();
    }

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
        }

        // Debugging console output
        System.out.println("Database exists. Initialization complete");
    }

    // "addAccount(...)" is used to add new accounts to the database
    // TODO: "addTransaction(...)" method; should be very similar to "addAccount(...)"
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
    // TODO: IMPORTANT: FIND WAY TO CLOSE CONNECTION AND STATEMENTS!
    // Mostly done, only need to figure out way to do it for returning ResultSet for displayAccounts()

}
