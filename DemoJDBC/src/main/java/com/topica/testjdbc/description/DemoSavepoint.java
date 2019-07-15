package com.topica.testjdbc.description;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import com.topica.testjdbc.DemoJDBC.ConnectionMySQL;

public class DemoSavepoint {
	static final String CREAT_STATEMENT = "Creating statement...";
	static final String LIST_RESULT = "List result set for reference....";
	static final String DEL_ROW = "Deleting row....";
	static final String QUERY = "SELECT id, name FROM student";
	static final String COMMIT_DATA = "Commiting data here....";
	static final String INSERT_ROW = "Inserting one row....";
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Connection conn = ConnectionMySQL.getMySQLConnection();
		Statement stmt = null;
		try {
			conn.setAutoCommit(false);
			System.out.println(CREAT_STATEMENT);
			stmt = conn.createStatement();

			String sql = QUERY;
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(LIST_RESULT);
			printRs(rs);

			Savepoint savepoint1 = conn.setSavepoint("ROWS_DELETED_1");
			System.out.println(DEL_ROW);
			String SQL = "DELETE FROM student " + "WHERE ID = 3";
			stmt.executeUpdate(SQL);

//			conn.rollback(savepoint1);

			Savepoint savepoint2 = conn.setSavepoint("ROWS_DELETED_2");
			System.out.println(DEL_ROW);
			SQL = "DELETE FROM student " + "WHERE ID = 2";
			stmt.executeUpdate(SQL);
			conn.rollback(savepoint2);
			conn.commit();
			
			

			sql = QUERY;
			rs = stmt.executeQuery(sql);
			System.out.println(LIST_RESULT);
			printRs(rs);
			
			// INSERT a row into student table
		      System.out.println(INSERT_ROW);
		      String sql2 = "INSERT INTO student " +
		                    "VALUES (6, 'Rita')";
		      stmt.executeUpdate(sql2);  
		      
		      //STEP 7: INSERT one more row into Employees table
		  
		      SQL = "INSERT INTO student " +
		                    "VALUES (7, 'Sita')";
		      stmt.executeUpdate(SQL);
		      //STEP 8: Commit data here.
		      System.out.println(COMMIT_DATA);
		      conn.commit();
		      conn.close();
			  
			  //STEP 9: Now list all the available records.
		      Connection conn1 = ConnectionMySQL.getMySQLConnection();
		      CallableStatement a = conn1.prepareCall("{Call GetStudent()}");
		      ResultSet rs2 = a.executeQuery();
		      System.out.println(LIST_RESULT);
		      printRs(rs2);
		      rs.close();
		      rs2.close();
		      stmt.close();
		      conn1.close();
			
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("Rolling back data here....");
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException se2) {
				se2.printStackTrace();
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			
		}
		System.out.println("Goodbye!");
	}

	public static void printRs(ResultSet rs) throws SQLException {	
		rs.beforeFirst();
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			
			System.out.print("ID: " + id);
			System.out.println(", Name: " + name);
		}
		System.out.println();
	}
}
