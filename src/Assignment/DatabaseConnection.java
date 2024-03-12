
package Assignment;

import com.mysql.jdbc.*;
import java.sql.*;

public class DatabaseConnection {
    public Connection dbConnection(){
        //String jdbcUrl = "jdbc:mysql://localhost:8080";
        String jdbcUrl = "jdbc:mysql://localhost:3306/customer" ;
        String username = "root";
        String password = "root";
        
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to the databse successfully!");
        }catch(SQLException e){
            e.printStackTrace(); 
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        
        return connection; 
    }
    
    public static void main(String args[]){
        DatabaseConnection obj = new DatabaseConnection();
        obj.dbConnection();
    }
}
