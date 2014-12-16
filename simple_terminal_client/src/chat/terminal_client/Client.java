package chat.terminal_client;

import chat.shared.Contact;
import chat.shared.IClientRemoteFacade;
import chat.shared.IServerRemoteFacade;
import chat.shared.Message;

import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by alex on 12/6/14.
 */
public class Client implements IClientRemoteFacade {
    private final IServerRemoteFacade serverRemoteFacade;
    private final Contact clientContact;

    public Client(IServerRemoteFacade serverRemoteFacade,Contact clientContact) {
        this.serverRemoteFacade = serverRemoteFacade;
        this.clientContact = clientContact;
    }

    @Override
    public void onNewMessage(Message message) {
        System.out.println(String.format("%1$s (%2$s): %3$s", message.getSender().getNickName(),
                StandartDateFormatter.formatDate(message.getSendingDate()),
                message.getText()));
    }

    @Override
    public void onContactOnline(Contact contact) {

    }

    @Override
    public void onContactWentOffline(Contact contact) {

    }

    public void sendMessage(String text){
        if(text != null && !text.equals("")){
            Message message = new Message(this.clientContact, Contact.GLOBAL_CHAT, Message.Type.CHAT, text, new Date());
            try {
                this.serverRemoteFacade.deliverMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void goOffline(){
        try {
            this.serverRemoteFacade.logoutContact(this.clientContact);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
