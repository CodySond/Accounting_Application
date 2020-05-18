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

        Statement state = con.createStatement();
        ResultSet res = state.executeQuery("SELECT * FROM accounts");
        return res;
    }

    private void getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        con = DriverManager.getConnection("jdbc:h2:~/AccountingApp.db");
        initialize();
    }

    private void initialize() throws SQLException {
        // TODO: Make way to detect if it has data
        if (!hasData){
            hasData = true;

            Statement state = con.createStatement();
            ResultSet res = state.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='TABLE' AND TABLE_NAME='ACCOUNTS'"); // NOTE: NOT SURE IF THIS ACTUALLY WORKS OR NOT
            if(!res.next()){
                System.out.println("Building sample database");
                //Build table
                Statement state2 = con.createStatement();
                state2.execute("CREATE TABLE IF NOT EXISTS accounts(id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "aname VARCHAR(60)," + "atype VARCHAR(60)," + "avalue INT);"
                        /*+ "primary key(id));"*/);

                //Insert default data
                PreparedStatement prep = con.prepareStatement("INSERT INTO accounts(id,aname,atype,avalue) VALUES(0,'Revenue', 'revenue', 0);");
                prep.execute();
                /*
                prep.setString(2, "Revenue");
                prep.setString(3, "revenue");
                prep.setInt(3, 0);
                */
            }
        }
        System.out.println("Database exists. Initialization complete");
    }

    public void addAccount(String accountName, String accountType, int accountValue) throws SQLException, ClassNotFoundException {
        if(con == null){
            getConnection();
        }

        PreparedStatement prep = con.prepareStatement("INSERT INTO accounts(?,?,?,?);");
        prep.setString(2, accountName);
        prep.setString(3, accountType);
        prep.setInt(4, accountValue);
        prep.execute();
    }
}
