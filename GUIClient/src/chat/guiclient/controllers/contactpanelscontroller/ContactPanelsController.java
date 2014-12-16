package chat.guiclient.controllers.contactpanelscontroller;

import chat.guiclient.Colors;
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
            addToPanel(contactPanel, panelForContactsPanel.getComponentCount() - 1);
        }
    }

    private void addToPanel(ContactPanel contactPanel, int index) {
        panelForContactsPanel.add(contactPanel, index);// panelForContactsPanel.getComponentCount() - 1);
        JSeparator separator = this.createSeparator();
        panelForContactsPanel.add(separator, index + 1);//panelForContactsPanel.getComponentCount() - 1);
    }

    @Override
    public void movePanelForContactToTop(Contact contact) {
        if(this.mapper.containsKey(contact)){
            ContactPanel panel = this.mapper.get(contact);
            this.removePanel(panel);
            this.addToPanel(panel, 0);
        }
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(Colors.CLOUDS.darker());
        separator.setOrientation(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new java.awt.Dimension(32767, 1));
        separator.setPreferredSize(new java.awt.Dimension(50, 1));
        return separator;
    }

    @Override
    public void removeContact(Contact contact) {
        if(this.mapper.containsKey(contact)) {
            final ContactPanel panel = this.mapper.remove(contact);
            this.removePanel(panel);
        }
    }

    private void removePanel(ContactPanel panel) {
        for(int i = panelForContactsPanel.getComponentCount() - 1; i>=0; i--){
            if(this.panelForContactsPanel.getComponent(i) == panel){
                this.panelForContactsPanel.remove(i);
                {this.panelForContactsPanel.remove(i);}//если элемент не первый в списке - удаляем лишний пробел перед ним
                break;
            }
        }
        panelForContactsPanel.remove(panel);
    }

    @Override
    public void markAsWithNewMessagePanelFor(Contact contact) {
        if(this.mapper.containsKey(contact)){
            ContactPanel contactPanel = this.mapper.get(contact);
            contactPanel.markAsWithNewMessages();
            this.removePanel(contactPanel);
            this.addToPanel(contactPanel, 2);
        }
    }

    @Override
    public void setActiveForPanelForContact(Contact contact, boolean value) {
        if(this.mapper.containsKey(contact)){
            this.mapper.get(contact).setActiveChatPanel(value);
            if(value) { this.movePanelForContactToTop(contact); }
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
