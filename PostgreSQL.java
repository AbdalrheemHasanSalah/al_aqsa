package sql;

//import component.Mainin;
import al_aqsa_project.Main;
import al_aqsa_project.NewJFrame;
import al_aqsa_project.encryption_Util;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Annotation;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DragSource;
import java.sql.Timestamp;  
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;

public class PostgreSQL  {

    // Database connection parameters
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/project";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123321";
JComboBox comboBox=new JComboBox();

public static String  id_post;


    // Function to establish a database connection
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    // Function to insert data into the database
    public static void insertData(String Name, String Identity,String birthdate,String phone,String Password) {
        String sql = "INSERT INTO visitor (name, date_birth, phone, id, password) VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";

        Password=encryption_Util.encrypt(Password);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Name);
            pstmt.setString(2, birthdate);
            pstmt.setString(3, phone);
            pstmt.setString(4, Identity);
            pstmt.setString(5, Password);

            pstmt.executeUpdate();
            System.out.println("Data inserted successfully.");

        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
    
      public static void UpdateData(String Name, String Identity,String birthdate,String phone,String Password) throws SQLException {
    String selectQuery = "SELECT * FROM visitor WHERE Id = ?";
            Password=encryption_Util.encrypt(Password);

      try (Connection conn = connect();
        PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
       selectStmt.setString(1,Identity ); // Set the value for the condition
    try (ResultSet rs = selectStmt.executeQuery()) {
        while (rs.next()) {
            // Step 2: Update the rows
String updateQuery = "UPDATE visitor SET name = '" + Name + "' , date_birth = '" + birthdate + "' , phone = '" + phone + "' , password = '" + Password + "' WHERE id = '" + Identity + "';";

try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                
                updateStmt.executeUpdate();
                JOptionPane.showMessageDialog(null,"UPDATE data Successful");

            }catch (SQLException e) {                                               
                       JOptionPane.showMessageDialog(null,"Error Updating data"+e.getMessage());

        }
        }
    }
}
      
      }
       public  void deleteData(int x) {
                  String deleteSQL = "DELETE FROM relation_visitor_location WHERE pk_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
             pstmt.setInt(1, x);
           

            pstmt.executeQuery();


        } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null,"delete data Successful");


        }
    }
       
       
    public static int  count(){ 
       String SQL = "SELECT COUNT(*) AS total FROM relation_visitor_location";
  int rowCount = 0;
       try (Connection conn = connect();
     PreparedStatement pstmt = conn.prepareStatement(SQL)) {
    ResultSet rs = pstmt.executeQuery();
    if (rs.next()) {
        rowCount = rs.getInt("total");
    }
    else{
        rowCount = 0;
    }
} catch (SQLException e) {
    e.printStackTrace();}
return rowCount;
    }
public static void insertData_relation(String location_name, String date,String visitor_id) {
                  String SQL = "INSERT INTO relation_visitor_location (location_name, date, visitor_id, exact_time) VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
                         Timestamp instant= Timestamp.from(Instant.now());  
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, location_name);
            pstmt.setString(2, date);
            pstmt.setString(3, visitor_id);
            pstmt.setTimestamp(4, instant);

            pstmt.executeUpdate();
                                   JOptionPane.showMessageDialog(null,"inserting data Successful");


        } catch (SQLException e) {
                       JOptionPane.showMessageDialog(null,"Error inserting data"+e.getMessage());

        }
    }
     public static void signin_password(String Identity,String password) throws SQLException, ClassNotFoundException{
      String sqlquery="SELECT * FROM visitor WHERE id='"+ Identity+"'";
     try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(sqlquery)){
        ResultSet rs = pst.executeQuery();
        if(!rs.next()){
                        JOptionPane.showMessageDialog(null,"invalid Identity");
        }
        else
        {
            String Encrypted=rs.getString("password");
            String EncrPass =encryption_Util.encrypt(password);
            if(EncrPass == null ? Encrypted == null : EncrPass.equals(Encrypted)){
           JOptionPane.showMessageDialog(null,"Login Successful");
            
          Main f1=new Main();
          f1.close();
            NewJFrame f2 = new NewJFrame();
                  f2.setVisible(true);
                                      id_post=Identity;

        }else{
                JOptionPane.showMessageDialog(null,"invalid Password");
            }} } 
        }
    
     public  ArrayList insert_table_visitor_show(){  
        String sqlQuery = "SELECT location_name, date, pk_id, exact_time FROM relation_visitor_location WHERE visitor_id = ?";
ArrayList<type> dataArray = new ArrayList<>();

try (Connection conn = connect();
     PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {

    // Set the parameter value (assuming id is an integer)
    preparedStatement.setString(1, id_post);

    // Execute the query
    ResultSet resultSet = preparedStatement.executeQuery();

    // Process the ResultSet
    while (resultSet.next()) {
        type a = new type();
        a.location_name = resultSet.getString("location_name");
        a.date = resultSet.getString("date"); // Assuming date is stored as a string
        a.exact_time = resultSet.getTimestamp("exact_time");
        a.pk_id = resultSet.getInt("pk_id");
        dataArray.add(a);
    }

    // Close the ResultSet
    resultSet.close();
} catch (Exception e) {
    e.printStackTrace();
}

return dataArray;
     }
     
      public  static ArrayList Select_Location() {
  String sql = "SELECT location_group  FROM public.locations";
ArrayList<String> dataArray = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {

            // Count the number of rows in the result set

            // Create an array to hold the data
            // Populate the array with data from the result set
            int rowIndex = 0;
            while (rs.next()) {
                String column1Value = rs.getString("location_group");
                // Insert data into the array
                dataArray.add(column1Value) ;
                rowIndex++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return dataArray;

    }
    public  static ArrayList Select_Location_type() {
  String sql = "SELECT location_type FROM public.locations";
ArrayList<String> dataArray = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {

            // Count the number of rows in the result set

            // Create an array to hold the data
            // Populate the array with data from the result set
            int rowIndex = 0;
            while (rs.next()) {
                String column1Value = rs.getString("location_type");
                // Insert data into the array
                dataArray.add(column1Value) ;
                rowIndex++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return dataArray;

    }
         public  static ArrayList Select_Location_name() {
  String sql = "SELECT name FROM public.locations";
ArrayList<String> dataArray = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {

            // Count the number of rows in the result set

            // Create an array to hold the data
            // Populate the array with data from the result set
            int rowIndex = 0;
            while (rs.next()) {
                String column1Value = rs.getString("name");
                // Insert data into the array
                dataArray.add(column1Value) ;
                rowIndex++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return dataArray;

    }
        
     
       
        
     
    // Function to close the database connection
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

}
