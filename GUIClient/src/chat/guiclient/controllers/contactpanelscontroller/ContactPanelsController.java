package chat.guiclient.controllers.contactpanelscontroller;

import chat.guiclient.ServiceLocator;
import chat.guiclient.controllers.chatscontrollsystem.IChatsController;
import chat.shared.Contact;

import javax.swing.*;
import java.util.HashMap;

/**
 * Created by alex on 12/7/14.
 */
public class ContactPanelsController implements IContactPanelsController{
    private final HashMap<Contact, ContactPanel> mapper;
    private final JPanel panelForContactsPanel;
    private IChatsController chatsController;

    public ContactPanelsController(JPanel panelForContactsPanel) {
        this.panelForContactsPanel = panelForContactsPanel;
        this.panelForContactsPanel.setLayout(new BoxLayout(this.panelForContactsPanel, BoxLayout.PAGE_AXIS));
        this.panelForContactsPanel.add(Box.createVerticalGlue());
        this.mapper = new HashMap<>();
    }

    @Override
    public void addNewContact(final Contact contact) {
        if (!this.mapper.containsKey(contact)) {
            ContactPanel contactPanel = new ContactPanel(chatsController, contact);
            mapper.put(contact, contactPanel);
            panelForContactsPanel.add(contactPanel, panelForContactsPanel.getComponentCount() - 1);
        }
    }

    @Override
    public void removeContact(Contact contact) {
        if(this.mapper.containsKey(contact)) {
            final ContactPanel panel = this.mapper.remove(contact);
            panelForContactsPanel.remove(panel);
        }
    }

    @Override
    public void markAsWithNewMessagePanelFor(Contact contact) {
        if(this.mapper.containsKey(contact)){
            this.mapper.get(contact).markAsWithNewMessages();
        }
    }

    public void refreshPanel(){
        this.panelForContactsPanel.revalidate();
        this.panelForContactsPanel.repaint();
    }

    @Override
    public void injectDependencies() {
        this.chatsController = ServiceLocator.getChatsController();
    }
}
