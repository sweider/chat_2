package chat.server;

import chat.shared.Contact;
import chat.shared.IClientRemoteFacade;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by alex on 12/6/14.
 */
public class OnlineContactsStorage {
    private final HashMap<Contact, IClientRemoteFacade> storage;

    public OnlineContactsStorage() {
        this.storage = new HashMap<>();
    }

    public void addOnlineContact(Contact contact, IClientRemoteFacade clientFacade){
        this.storage.put(contact, clientFacade);
    }

    public IClientRemoteFacade removeContactFromOnlineContacts(Contact contact){
        return this.storage.remove(contact);
    }

    public IClientRemoteFacade getRemoteFacadeForContact(Contact contact){
        return this.storage.get(contact);
    }

    public Iterable<IClientRemoteFacade> getAllOnlineContactsRemoteFacades(){
        return this.storage.values();
    }

    public Collection<Contact> getAllOnlineContacts(){ return this.storage.keySet(); }
    public boolean isContactAlreadyOnline(Contact contact){
        return this.storage.containsKey(contact);
    }
}
