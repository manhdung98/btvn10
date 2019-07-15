package com.topica.testjdbc.DemoJDBC;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReturnTable {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Connection conn = ConnectionMySQL.getMySQLConnection();
		CallableStatement a = conn.prepareCall("{Call GetStudent()}");
		ResultSet resultSet = a.executeQuery();
		while (resultSet.next()) {
			System.out.println(resultSet.getInt(1) + " - " + resultSet.getString(2));
		}

	}
}
