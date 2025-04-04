package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {


    public Account addAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            
        String sql = "SELECT * FROM account WHERE username = ?;";
        PreparedStatement prepStmnt = connection.prepareStatement(sql);
        prepStmnt.setString(1, account.getUsername());
        ResultSet rs = prepStmnt.executeQuery();
            
        if (rs.next()) {
            return null; 
        }
        if (account.getUsername().isEmpty()|| account.getPassword().length() < 4) {
            return null; 
        }

        String sql2 = "INSERT INTO account (username, password) VALUES (?, ?);";
        PreparedStatement prepStmnt2 = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
        prepStmnt2.setString(1, account.getUsername());
        prepStmnt2.setString(2, account.getPassword());

        prepStmnt2.executeUpdate();
        ResultSet pkeyResultSet = prepStmnt2.getGeneratedKeys();

        if (pkeyResultSet.next()) {
            int generatedId = pkeyResultSet.getInt(1);
            return new Account(generatedId, account.getUsername(), account.getPassword());
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account checkLogOn(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement prepStmnt = connection.prepareStatement(sql);
            prepStmnt.setString(1, account.getUsername());
            prepStmnt.setString(2, account.getPassword());
    
            ResultSet rs = prepStmnt.executeQuery();
    
            if (rs.next()) { 
                return new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password"));
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }
}
