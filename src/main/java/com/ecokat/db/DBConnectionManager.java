/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecokat.db;

import java.sql.*;
import java.util.ResourceBundle;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author iozen
 */
public class DBConnectionManager {
    private Connection conn;
    private ResourceBundle rb = ResourceBundle.getBundle("application");

    public Connection getConnection() throws SQLException, ClassNotFoundException, NamingException {
        if (conn == null || conn.isClosed()) {
            InitialContext context = new InitialContext();
            DataSource ds = (DataSource) context.lookup(rb.getString("jndi"));
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            return conn;
        } else {
            return conn;
        }
    }
    public void rollBack() {
        try {
            conn.rollback();
        } catch (Exception ex) {
        }
    }
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception ex) {
        }
    }
    public void closePSStatement(PreparedStatement pstmt) {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (Exception ex) {
        }
    }
    public void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception ex) {
        }
    }
    public void closeResultSet(ResultSet rset) {
        try {
            if (rset != null) {
                rset.close();
            }
        } catch (Exception ex) {
        }
    }
}
