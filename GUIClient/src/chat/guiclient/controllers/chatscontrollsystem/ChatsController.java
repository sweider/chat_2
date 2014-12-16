package chat.guiclient.controllers.chatscontrollsystem;

import chat.guiclient.ClientGUI;
import chat.guiclient.Colors;
import chat.guiclient.ServiceLocator;
import chat.guiclient.StandartDateFormatter;
import chat.guiclient.controllers.contactpanelscontroller.IContactPanelsController;
import chat.shared.Contact;
import chat.shared.Message;

import javax.swing.*;
import javax.swing.text.*;
import java.util.HashMap;

/**
 * Created by alex on 12/6/14.
 */
public class ChatsController implements IChatsController {
    private final static SimpleAttributeSet OWN_MESSAGE_HEADER_ATTRIBUTES;
    private final static SimpleAttributeSet OTHERS_MESSAGE_HEADER_ATTRIBUTES;
    private final static SimpleAttributeSet MESSAGE_TEXT_ATTRIBUTES;
    public static final String SPACE = " ";
    public static final String NEW_LINE = "\n";
    static {
        OWN_MESSAGE_HEADER_ATTRIBUTES = new SimpleAttributeSet();
        OWN_MESSAGE_HEADER_ATTRIBUTES.addAttribute(StyleConstants.Foreground, Colors.PETER_RIVER);
        OWN_MESSAGE_HEADER_ATTRIBUTES.addAttribute(StyleConstants.Bold, true);

        OTHERS_MESSAGE_HEADER_ATTRIBUTES = new SimpleAttributeSet();
        OTHERS_MESSAGE_HEADER_ATTRIBUTES.addAttribute(StyleConstants.Foreground, Colors.ALIZARIN);
        OTHERS_MESSAGE_HEADER_ATTRIBUTES.addAttribute(StyleConstants.Bold, true);

        MESSAGE_TEXT_ATTRIBUTES = new SimpleAttributeSet();
        MESSAGE_TEXT_ATTRIBUTES.addAttribute(StyleConstants.Foreground, Colors.TEXT_FOREGROUND);
    }

    private final HashMap<Contact, StyledDocument> mapper;
    private final ClientGUI clientGui;
    private final Contact self;
    private IContactPanelsController contactPanelsController;
    private Contact currentDialogContact;

    public ChatsController(ClientGUI clientGui, Contact self) {
        this.clientGui = clientGui;
        this.self = self;
        this.mapper = new HashMap<>();
    }

    @Override
    public void activateChatForContact(final Contact contact) {
        if(!contact.equals(this.currentDialogContact)) {
            this.addIfNotExist(contact);
            this.contactPanelsController.setActiveForPanelForContact(this.currentDialogContact, false);
            this.currentDialogContact = contact;
            final String chatCaption = contact.equals(Contact.GLOBAL_CHAT) ? "Global chat" : "Chat with " + contact.getNickName();
            clientGui.setAsCurrentChatDocument(mapper.get(contact));
            clientGui.setAsCurrentChatCaption(chatCaption);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    contactPanelsController.setActiveForPanelForContact(contact, true);
                    contactPanelsController.refreshPanel();
                }
            };
            SwingUtilities.invokeLater(r);
        }
    }

    @Override
    public void addMessageToChat(Message message) {
        switch (message.getType()){
            case PRIVATE: this.addPrivateChatMessage(message);break;
            case CHAT: this.addGlobalChatMessage(message); break;
            default: assert false;
        }
        boolean messageToCurrentlyActiveChat = message.getSender().equals(currentDialogContact) && message.getReceiver().equals(self);
        boolean ownMessage = message.getSender().equals(self);
        if(!messageToCurrentlyActiveChat && !ownMessage){
            final Contact contact = message.getType() == Message.Type.CHAT ? Contact.GLOBAL_CHAT : message.getSender();
            Runnable r = new Runnable() {
                @Override public void run() {
                    contactPanelsController.markAsWithNewMessagePanelFor(contact);
                }
            };
            SwingUtilities.invokeLater(r);
        }
    }

    @Override
    public void resetCurrent() {
        this.activateChatForContact(this.currentDialogContact);
    }

    @Override
    public Contact getCurrentDialogContact() {
        return currentDialogContact;
    }

    @Override
    public void injectDependencies() {
        this.contactPanelsController = ServiceLocator.getContactPanelsController();
    }

    private void addIfNotExist(Contact contact) {
        if(!this.mapper.containsKey(contact)){
            this.mapper.put(contact, new DefaultStyledDocument());
        }
    }

    private void addPrivateChatMessage(Message message) {
        boolean isOwnMessage = message.getSender().equals(this.self);
        Contact contactToFind = isOwnMessage ? message.getReceiver() : message.getSender();
        this.addIfNotExist(contactToFind);
        StyledDocument doc = this.mapper.get(contactToFind);
        this.addMessageToDocument(doc, message,isOwnMessage);
    }

    private void addGlobalChatMessage(Message message) {
        StyledDocument doc = this.mapper.get(Contact.GLOBAL_CHAT);
        boolean isOwnMessage = message.getSender().equals(this.self);
        this.addMessageToDocument(doc, message,isOwnMessage);
    }

    void addMessageToDocument(StyledDocument document, Message message, boolean isOwnMessage){
        try {
            SimpleAttributeSet headerAttributes = isOwnMessage ? OWN_MESSAGE_HEADER_ATTRIBUTES : OTHERS_MESSAGE_HEADER_ATTRIBUTES;
            document.insertString(document.getLength(), message.getSender().getNickName() + SPACE, headerAttributes);
            document.insertString(document.getLength()
                    , "(" + StandartDateFormatter.formatDate(message.getSendingDate()) + "): "
                    , headerAttributes);
            document.insertString(document.getLength(), message.getText()+ NEW_LINE, MESSAGE_TEXT_ATTRIBUTES);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
