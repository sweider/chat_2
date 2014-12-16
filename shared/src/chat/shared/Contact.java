package chat.shared;

import java.io.Serializable;

/**
 * Класс, представляющий пользователя чата. На данный момент прост как грабли
 * и введен на случай возможного расширения логики контакта
 * Created by alex on 12/6/14.
 */
public class Contact implements Serializable{
    public final static Contact GLOBAL_CHAT = new Contact("Global chat");
    private final String nickName;

    public Contact(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (nickName != null ? !nickName.equals(contact.nickName) : contact.nickName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nickName != null ? nickName.hashCode() : 0;
    }
}
