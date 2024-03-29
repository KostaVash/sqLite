
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.sql.DatabaseMetaData;
import java.*;

/**
 *
 * @author sqlitetutorial.net
 */

/**

First level:
	TODO: Build one class for the main   
	TODO: Build class that send request to SqLite and got recived (Class 2)
Second level:
	TODO: Build class that take data and decode data to query.
	TODO: Build mapping table (recomend use 2 columns(first name of table, second save data type))
	TODO: Build class for interactive menu for the user. The menu will send data to Class 2 
	TODO: In class 2 build engine that work with mapping table.
	
	My recomendation use json to mapping.
	Add log file. In log recomend take time_stap, class name and operation name. In case of failed write the error.
	
*/
public class SqLite {
	static String JDBC_URL="jdbc:sqlite:C:/sqlite/db/";
	static Connection conn = null;
	private  String tableName;
	private  List<String> columnName =new LinkedList();
	 
	/**
	 * set the db path
	 * @param fileName
	 */
	public static void setUrl(String fileName) {
		JDBC_URL="jdbc:sqlite:C:/sqlite/db/"+fileName+".db";
	}
	 /**
     * Connect to a sample database
     */
	/**
	 * 
	
    public static void connect() {
      
        try {
            // db parameters
          
            // create a connection to the database
            conn = DriverManager.getConnection(JDBC_URL);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
	 */
    
    private Connection connect() {
        // SQLite connection string
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    
	 /**
     * Connect to a sample database
     *
     * @param fileName the database file name
     */
	
	 public  void createNewDatabase() {
		 
	        
	 
	        try (Connection conn = this.connect()) {
	            if (conn != null) {
	                DatabaseMetaData meta = conn.getMetaData();
	                System.out.println("The driver name is " + meta.getDriverName());
	                System.out.println("A new database has been created.");
	            }
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }
	 /**
	     * Create a new table in the test database
	     *
	     */
    public  void createNewTable(String tableName) {
        // SQLite connection string
       
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS "+tableName+" (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    name text NOT NULL,\n"
                + "    capacity real\n"
                + ");";
        
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	      getColumnNames();
    }
 
/**
	 * Create a new column in table
	 * *TODO:to add more data types and size and constraints
	 */
	public void createNewColumn(String name) {
		int checkExsits;
		checkExsits=getString(name);
		if(checkExsits==1) {
		System.out.println("enter a number to choose the data type you want \n "+
				"1 - INT/BIGINT \n"+
				"2 - DOUBLE \n"+""
				+ "3 -DATETIME \n"+""
				+ "4 -CHAR \n"+
				"5 -TEXT	\n"+
				"enter your choose: "
				);
		Scanner s = new Scanner(System.in);
		int i = s.nextInt();
		String type="";
		
		switch(i) {
		case 1: 
			type="BIGINT;";
			break;
		case 2:
			type="DOUBLE;";
			break;
		case 3:
			type="DATETIME;";
			break;
			case 4:
				type="CHAR;";
			break;
			case 5:
				type="TEXT;";
				break;
			
		}
		

		
		// SQL statement for creating a new column
		String sql = "ALTER TABLE "+this.tableName+"\n"
				+"ADD COLUMN "+name+" "+type;
		System.out.println(sql);

		try (Connection conn = this.connect();
				Statement stmt = conn.createStatement()) {
			// create a new column
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		}
		else {
			System.out.println("this column name already exsits");
		}
	}
		

    /**
     * Insert a new row into the warehouses table
     *
     * @param name
     * @param capacity
     */
    public void insert(String tableName,String name, double capacity) {
        String sql = "INSERT INTO "+tableName+"(name,capacity) VALUES(?,?)";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());}
        }
    /**
     * @param args the command line arguments
     */
    
    public void selectAll(){
        String sql = "SELECT id, name, capacity FROM warehouses";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("name") + "\t" +
                                   rs.getDouble("capacity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
    }
    public void getCapacityGreaterThan(double capacity){
        String sql = "SELECT id, name, capacity "
                   + "FROM warehouses WHERE capacity > ?";
 
 try (Connection conn = this.connect();
      PreparedStatement pstmt  = conn.prepareStatement(sql)){
     
     // set the value
     pstmt.setDouble(1,capacity);
    
     //
     ResultSet rs  = pstmt.executeQuery();
     
     // loop through the result set
     while (rs.next()) {
         System.out.println(rs.getInt("id") +  "\t" + 
                            rs.getString("name") + "\t" +
                            rs.getDouble("capacity"));
     }
 } catch (SQLException e) {
     System.out.println(e.getMessage());
 }}
    /**
	 * add the column names from the table
	 */
	private void getColumnNames() {
		String sql="SELECT * FROM "+tableName;
		 
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
            
           ResultSet rs= stmt.executeQuery(sql);
           ResultSetMetaData mrs=rs.getMetaData();
           for(int i=1;i<=mrs.getColumnCount();i++) {
        	   columnName.add(mrs.getColumnLabel(i));
           }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	/**
	 * check if the name already exsits in the column table
	 * return 1 if added 
	 * retrun 0 if already exsits {
	 */private int getString(String name) {
		if(columnName.isEmpty()) {
			columnName.add(name);
			
			return 1;
		}
		else {if(columnName.contains(name)) {
			
			return 0;
		}
		else {
			columnName.add(name);
			
			return 1;
		}
			
			
				
		}
	
	}
    public static void main(String[] args) {

SqLite test=new SqLite();
test.setUrl("test2");
   test.createNewDatabase();
   test.createNewTable("warehouses");
//test.connect();
  // test.insert("warehouses","test1 test1", 3000);
//  test.insert("warehouses","test2 test2", 4000);
//    test.insert("warehouses","test3 test3", 5000);
//   test.selectAll();
//    test.getCapacityGreaterThan(3600);
   
 
    }
 
}
