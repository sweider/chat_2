package chat.guiclient;

import chat.guiclient.controllers.chatscontrollsystem.IChatsController;
import chat.guiclient.controllers.contactpanelscontroller.IContactPanelsController;

/**
 * Created by alex on 12/7/14.
 */
public class ServiceLocator {
    private static IChatsController chatsController;
    private static IContactPanelsController contactPanelsController;

    public static void initialize(IChatsController chatsController, IContactPanelsController contactPanelsController){
        ServiceLocator.chatsController = chatsController;
        ServiceLocator.contactPanelsController = contactPanelsController;
    }

    public static IChatsController getChatsController() {
        return chatsController;
    }

    public static IContactPanelsController getContactPanelsController() {
        return contactPanelsController;
    }
}
