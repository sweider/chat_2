package chat.guiclient.controllers.contactpanelscontroller;

import chat.guiclient.controllers.IController;
import chat.shared.Contact;

/**
 * Created by alex on 12/7/14.
 */
public interface IContactPanelsController extends IController {
    void addNewContact(Contact contact);
    void removeContact(Contact contact);
    void markAsWithNewMessagePanelFor(Contact contact);
    void refreshPanel();
}
