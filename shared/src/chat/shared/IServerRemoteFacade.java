package chat.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Удаленный фасад сервера, предназначенный для взаимодействия
 * клиентской стороны с сервером.
 * Created by alex on 12/6/14.
 */
public interface IServerRemoteFacade extends Remote{
    /**
     * Данный метод должен доставить клиентское сообщение адресату, который указан в оном.
     * @param message сообщение, которое должно быть доставлено
     */
    void deliverMessage(Message message) throws RemoteException;

    /**
     * Добавить контакт в список онлайн контактов.
     * Возвращает истину, если такой контакт еще не в сети, ложь, если уже в сети.
     * @param contact
     * @param remoteFacade удаленный клиентский фасад, для взаимодействия с клиентом.
     * @return true, если логин успешен, false, если такой пользователь в сети.
     */
    boolean loginContact(Contact contact, IClientRemoteFacade remoteFacade) throws RemoteException;

    /**
     * Удаляет контакт из списка контактов онлайн.
     * @param contact
     */
    void logoutContact(Contact contact) throws RemoteException;

    Iterable<Contact> getOnlineContacts() throws RemoteException;
}
