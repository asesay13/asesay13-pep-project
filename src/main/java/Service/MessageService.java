package Service;

//import Model.Account;
import Model.Message;

import java.util.List;

import DAO.MessageDAO;

import Model.Account;


public class MessageService {
    
    MessageDAO messDAO;
    

    public MessageService() {
        messDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messDAO) {
        this.messDAO = messDAO;

    }
    public Message createAmessage(Message message) {
        return messDAO.newMesssage(message); 
    }

    public List<Message> AllMessages() {
        return messDAO.getAllMessages();
    }

    public Message getMessageID(int messageId) {
        return messDAO.selectAmessage(messageId); 
    }

    public Message deleteMessageID(int messageId) {
        return messDAO.deleteAmessage(messageId); 
    }

    public Message updateMessageById(int messageId, Message newMessage) {
        return messDAO.updateMessageVIAid(messageId, newMessage);
    }

    public List<Message> getAllMessagesByAccountId(int accountId) {
        return messDAO.getAllMessagesbyACC(accountId);
    }

}
