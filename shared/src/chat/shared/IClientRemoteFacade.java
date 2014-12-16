package chat.shared;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Удаленный фасад клиента, для взаимодествия сервера с клиентской частью
 * Created by alex on 12/6/14.
 */
public interface IClientRemoteFacade extends Serializable, Remote{
    /**
     * Обработка нового сообщения с сервера.
     * @param message сообщение
     */
    void onNewMessage(Message message) throws RemoteException;

    void onContactOnline(Contact contact) throws RemoteException;

    void onContactWentOffline(Contact contact) throws RemoteException;
}
