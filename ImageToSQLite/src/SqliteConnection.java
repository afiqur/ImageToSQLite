
import java.sql.*;
import java.sql.Connection;

import javax.swing.JOptionPane;


public class SqliteConnection 
{
	Connection conn=null;
	public static Connection dbConnector()
	{
		try{
			Class.forName("org.sqlite.JDBC");
			
			Connection conn = DriverManager.getConnection("jdbc:sqlite:DB//ImageToSQLite.sqlite");			
			
			JOptionPane.showMessageDialog(null, "Database OK");
			return conn;
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,"Wrong Entry, Try Again");
			return null;
		}
	}

}
