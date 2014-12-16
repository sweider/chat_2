package chat.guiclient;

import chat.guiclient.controllers.chatscontrollsystem.IChatsController;
import chat.guiclient.controllers.contactpanelscontroller.IContactPanelsController;
import chat.shared.Contact;
import chat.shared.IClientRemoteFacade;
import chat.shared.IServerRemoteFacade;
import chat.shared.Message;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Created by alex on 12/6/14.
 */
public class ClientFacade implements IClientRemoteFacade{
    private Contact currentContact;
    private final IServerRemoteFacade server;
    private IChatsController chatsController;
    private IContactPanelsController panelsController;
    private JFrame window;

    public ClientFacade(IServerRemoteFacade server) {
        this.server = server;
    }

    public void setCurrentContact(Contact currentContact) {
        this.currentContact = currentContact;
    }

    public void injectDependencies(){
        this.chatsController = ServiceLocator.getChatsController();
        this.panelsController = ServiceLocator.getContactPanelsController();
        try {
            Iterable<Contact> onlineContacts = this.server.getOnlineContacts();
            for(Contact contact : onlineContacts){
                if(!contact.equals(this.currentContact)) {
                    this.panelsController.addNewContact(contact);
                }
            }
            this.panelsController.refreshPanel();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.chatsController.activateChatForContact(Contact.GLOBAL_CHAT);
    }

    @Override
    public void onNewMessage(final Message message) throws RemoteException {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                chatsController.addMessageToChat(message);
            }
        };
        new Thread(r).start();
    }

    @Override
    public void onContactOnline(final Contact contact) throws RemoteException {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                panelsController.addNewContact(contact);
                panelsController.refreshPanel();
            }
        };
        java.awt.EventQueue.invokeLater(r);
    }

    @Override
    public void onContactWentOffline(final Contact contact) throws RemoteException {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                panelsController.removeContact(contact);
                panelsController.refreshPanel();
            }
        };
        SwingUtilities.invokeLater(r);
    }

    public void disconnectFromServer(){
        try {
            this.server.logoutContact(this.currentContact);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String text){
        Contact receiver = this.chatsController.getCurrentDialogContact();
        Message.Type type = receiver.equals(Contact.GLOBAL_CHAT) ? Message.Type.CHAT : Message.Type.PRIVATE;
        Message message = new Message(this.currentContact, receiver, type, text, new Date());
        try { this.server.deliverMessage(message); }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    boolean isSelf(Contact contact){
        return this.currentContact.equals(contact);
    }

    public void setFrame(ClientGUI clientGUI) {
        this.window = clientGUI;
    }
}
