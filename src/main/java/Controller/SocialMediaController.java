package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService AccService;
    MessageService MesService;

    public SocialMediaController() {
        AccService = new AccountService();
        MesService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::postRegistrationHandler);
        app.post("login", this::postLogInHandler);
        app.post("messages", this::postNewMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessagesbyIDHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::patchMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getAllmessagesbyIDHandler);
        

        return app;

    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postRegistrationHandler(Context ctx) throws JsonProcessingException{
        //ctx.json("sample text");
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account RegisterAccount = AccService.insertAccount(account);
        if(RegisterAccount==null){
            ctx.status(400);
        }
        else{
            ctx.json(mapper.writeValueAsString(RegisterAccount));
        }
    }

        private void postLogInHandler(Context ctx) throws JsonProcessingException{
            ObjectMapper mapper = new ObjectMapper();
    
    
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account loggedInAccount = AccService.LogonAccount(account);

            if (loggedInAccount != null) {
                ctx.status(200).json(loggedInAccount); 
            } 
                else {
                ctx.status(401).json(""); 
                }
        }

        private void postNewMessageHandler(Context ctx) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message createdMessage = MesService.createAmessage(message);

            if (createdMessage != null) {
                ctx.status(200).json(createdMessage); 
            }       
                else {
                    ctx.status(400).json(""); 
                }
         }  

         private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
            ctx.json(MesService.AllMessages());
        }

        private void getMessagesbyIDHandler(Context ctx) {
            int messageId = Integer.parseInt(ctx.pathParam("message_id")); 
            Message idMessage = MesService.getMessageID(messageId); 
        
            if (idMessage != null) {
                ctx.status(200).json(idMessage); 
            } else {
                ctx.status(200).json("");
            }
        }

        private void deleteMessageHandler(Context ctx) {
            int messageId = Integer.parseInt(ctx.pathParam("message_id")); 
            Message deletedMessage = MesService.deleteMessageID(messageId); 
            if (deletedMessage != null) {
                ctx.status(200).json(deletedMessage); 
            } else {
                ctx.status(200).json(""); 
            }
        }

        private void patchMessageByIdHandler(Context ctx) throws JsonProcessingException {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            ObjectMapper mapper = new ObjectMapper();
            Message MSG_text = mapper.readValue(ctx.body(), Message.class);
        
            Message updatedMessage = MesService.updateMessageById(messageId, MSG_text);
            if (updatedMessage != null) {
                ctx.status(200).json(updatedMessage);
            } else {
                ctx.status(400).json(""); 
            }
        }

        private void getAllmessagesbyIDHandler(Context ctx) {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = MesService.getAllMessagesByAccountId(accountId);
            ctx.status(200).json(messages); 
}
    
}     