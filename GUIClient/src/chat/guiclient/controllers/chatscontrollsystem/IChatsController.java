package chat.guiclient.controllers.chatscontrollsystem;

import chat.guiclient.controllers.IController;
import chat.shared.Contact;
import chat.shared.Message;

/**
 * Created by alex on 12/6/14.
 */
public interface IChatsController extends IController{
    public void activateChatForContact(Contact contact);
    public void addMessageToChat(Message message);
    public Contact getCurrentDialogContact();
    public void resetCurrent();
}
