package chat.guiclient.controllers.chatscontrollsystem;

import chat.guiclient.ClientGUI;
import chat.guiclient.ServiceLocator;
import chat.guiclient.StandartDateFormatter;
import chat.guiclient.controllers.contactpanelscontroller.IContactPanelsController;
import chat.shared.Contact;
import chat.shared.Message;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Created by alex on 12/6/14.
 */
public class ChatsController implements IChatsController {
    private final static SimpleAttributeSet OWN_MESSAGE_ATTRIBUTES;
    private final static SimpleAttributeSet OTHERS_MESSAGE_ATTRIBUTES;
    public static final String SPACE = " ";
    public static final String NEW_LINE = "\n";
    static {
        OWN_MESSAGE_ATTRIBUTES = new SimpleAttributeSet();
        OWN_MESSAGE_ATTRIBUTES.addAttribute(StyleConstants.Foreground, Color.BLUE);
        OWN_MESSAGE_ATTRIBUTES.addAttribute(StyleConstants.Bold, true);

        OTHERS_MESSAGE_ATTRIBUTES = new SimpleAttributeSet();
        OTHERS_MESSAGE_ATTRIBUTES.addAttribute(StyleConstants.Foreground, Color.RED);
        OTHERS_MESSAGE_ATTRIBUTES.addAttribute(StyleConstants.Bold, true);
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
            this.currentDialogContact = contact;
            final String chatCaption = contact.equals(Contact.GLOBAL_CHAT) ? "Global chat" : "Chat with " + contact.getNickName();
            clientGui.setAsCurrentChatDocument(mapper.get(contact));
            clientGui.setAsCurrentChatCaption(chatCaption);
        }
    }

    @Override
    public void addMessageToChat(Message message) {
        switch (message.getType()){
            case PRIVATE: this.addPrivateChatMessage(message);break;
            case CHAT: this.addGlobalChatMessage(message); break;
            default: assert false;
        }
        if(!message.getSender().equals(self) && !message.getSender().equals(currentDialogContact)){
            final Contact con = message.getType() == Message.Type.CHAT ? Contact.GLOBAL_CHAT : message.getSender();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    contactPanelsController.markAsWithNewMessagePanelFor(con);
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
            SimpleAttributeSet set = isOwnMessage ? OWN_MESSAGE_ATTRIBUTES : OTHERS_MESSAGE_ATTRIBUTES;
            document.insertString(document.getLength(), message.getSender().getNickName() + SPACE, set);
            document.insertString(document.getLength()
                    , "(" + StandartDateFormatter.formatDate(message.getSendingDate()) + "): "
                    , set);
            document.insertString(document.getLength(), message.getText()+ NEW_LINE, document.getStyle("regular"));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
