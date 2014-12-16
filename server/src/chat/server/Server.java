package chat.server;

import chat.shared.Contact;
import chat.shared.IClientRemoteFacade;
import chat.shared.IServerRemoteFacade;
import chat.shared.Message;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * Created by alex on 12/6/14.
 */
public class Server implements IServerRemoteFacade {
    private final OnlineContactsStorage onlineContactsStorage;

    public Server() {
        this.onlineContactsStorage = new OnlineContactsStorage();
    }

    @Override
    public void deliverMessage(Message message) {
        switch (message.getType()){
            case PRIVATE: this.deliverPrivateMessage(message);break;
            case CHAT: this.deliverGlobalChatMessage(message);break;
            default: assert false : "Unknown or not setted message type";
        }
    }

    @Override
    synchronized public boolean loginContact(Contact contact, IClientRemoteFacade remoteFacade) {
        if(this.onlineContactsStorage.isContactAlreadyOnline(contact)){ return false; }
        else {
            this.onlineContactsStorage.addOnlineContact(contact, remoteFacade);
            for(IClientRemoteFacade currentFacade : this.onlineContactsStorage.getAllOnlineContactsRemoteFacades()){
                if (remoteFacade != currentFacade) {
                    try {
                        currentFacade.onContactOnline(contact);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }
    }

    @Override
    synchronized public void logoutContact(Contact contact) {
        if(this.onlineContactsStorage.isContactAlreadyOnline(contact)){
            this.onlineContactsStorage.removeContactFromOnlineContacts(contact);
            for(IClientRemoteFacade remoteFacade : this.onlineContactsStorage.getAllOnlineContactsRemoteFacades()){
                try {
                    remoteFacade.onContactWentOffline(contact);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Iterable<Contact> getOnlineContacts() throws RemoteException {
        ArrayList<Contact> contacts = new ArrayList<Contact>(this.onlineContactsStorage.getAllOnlineContacts());
        contacts.add(Contact.GLOBAL_CHAT);
        return contacts;
    }

    private void deliverGlobalChatMessage(Message message) {
        for(IClientRemoteFacade remoteFacade : this.onlineContactsStorage.getAllOnlineContactsRemoteFacades()){
            try {
                remoteFacade.onNewMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void deliverPrivateMessage(Message message) {
        if(this.onlineContactsStorage.isContactAlreadyOnline(message.getReceiver())){
            IClientRemoteFacade receiverFacade = this.onlineContactsStorage.getRemoteFacadeForContact(message.getReceiver());
            IClientRemoteFacade senderFacade = this.onlineContactsStorage.getRemoteFacadeForContact(message.getSender());
            try {
                receiverFacade.onNewMessage(message);
                senderFacade.onNewMessage(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }



}
