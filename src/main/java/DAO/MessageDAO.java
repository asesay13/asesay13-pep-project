package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public Message newMesssage(Message message) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        if (message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            return null; 
        }
        String sql1 = "SELECT * FROM account WHERE account_id = ?";
        PreparedStatement prepStmnt1 = connection.prepareStatement(sql1);
        prepStmnt1.setInt(1, message.getPosted_by());
        ResultSet rs1 = prepStmnt1.executeQuery();
        if (!rs1.next()) {
            return null; 
        }

       

        String sql2 = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
        PreparedStatement prepStmnt2 = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
        prepStmnt2.setInt(1, message.getPosted_by());
        prepStmnt2.setString(2, message.getMessage_text());
        prepStmnt2.setLong(3, message.getTime_posted_epoch()); 
        prepStmnt2.executeUpdate();

            ResultSet rs = prepStmnt2.getGeneratedKeys();
            if (rs.next()) {
                int messageID = rs.getInt(1);
                return new Message(messageID, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
      
        } 
    catch (SQLException e) {
        e.printStackTrace(); 
    }
    return null; 
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement prepStmnt = connection.prepareStatement(sql);
            ResultSet rs = prepStmnt.executeQuery();
            
            while(rs.next()){
                int msgID = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String msgText = rs.getString("message_text");
                long timePosted = rs.getLong("time_posted_epoch");
                
                Message message = new Message(msgID, postedBy, msgText, timePosted);
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message selectAmessage(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement prepStmnt = connection.prepareStatement(sql);
            prepStmnt.setInt(1, messageId);
        
            ResultSet rs = prepStmnt.executeQuery();
        
            if (rs.next()) { 
                return new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return null; 
    }

    public Message deleteAmessage(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;
        try {

            String sql1 = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement prepStmnt = connection.prepareStatement(sql1);
            prepStmnt.setInt(1, messageId);
            ResultSet rs = prepStmnt.executeQuery();

            if (!rs.next()) { 
                return null; 
            }


            deletedMessage = new Message(
            rs.getInt("message_id"),
            rs.getInt("posted_by"),
            rs.getString("message_text"),
            rs.getLong("time_posted_epoch")
        );

        
        String sql2 = "DELETE FROM message WHERE message_id = ?";
        PreparedStatement prepStmnt2 = connection.prepareStatement(sql2);
        prepStmnt2.setInt(1, messageId);
        prepStmnt2.executeUpdate(); // Execute deletion

       
        return deletedMessage;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public Message updateMessageVIAid(int messageId, Message message) {
    Connection connection = ConnectionUtil.getConnection();
    Message updatedMessage = null;

    try {
        
        String sql1 = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement prepStmnt = connection.prepareStatement(sql1);
        prepStmnt.setInt(1, messageId);
        ResultSet rs = prepStmnt.executeQuery();

        if (!rs.next()) {
            return null; 
        }
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }

       
        String sql2 = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement prepStmnt2 = connection.prepareStatement(sql2);
        prepStmnt2.setString(1, message.getMessage_text());
        prepStmnt2.setInt(2, messageId);
        prepStmnt2.executeUpdate();

        
        PreparedStatement Updated = connection.prepareStatement(sql1);
        Updated.setInt(1, messageId);
        ResultSet updatedRs = Updated.executeQuery();

        if (updatedRs.next()) {
            updatedMessage = new Message(
                updatedRs.getInt("message_id"),
                updatedRs.getInt("posted_by"),
                updatedRs.getString("message_text"),
                updatedRs.getLong("time_posted_epoch")
            );
        }

                return updatedMessage;

            } 
            catch (SQLException e) {
                e.printStackTrace();
            }

                return null;
    }

    public List<Message> getAllMessagesbyACC(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> all_messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement prepStmnt = connection.prepareStatement(sql);
            prepStmnt.setInt(1, accountId);
            ResultSet rs = prepStmnt.executeQuery();
    
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                all_messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return all_messages;
    

    }
}


