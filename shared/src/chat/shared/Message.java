package chat.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by alex on 12/6/14.
 */
public class Message implements Serializable{
    /**
     * Тип сообщения определяет стратегию обработки на сервере. Для сообщений чата
     * используется простая схема обработки, для личных сообщений включается роутинг.
     */
    public static enum Type{

        /**
         * Сообщения данного типа отправляются конкретному участнику.
         * Необходимо обращать внимание на адресата, при обработке на сервере.
         */
        PRIVATE,


        /**
         * Сообщения данного типа отправляются в глобальный чат, можно не учитывать адресата.
         */
        CHAT
    }

    private final Contact sender;
    private final Contact receiver;
    private final Type type;
    private final String text;
    private final Date sendingDate;

    public Message(Contact sender, Contact receiver, Type type, String text, Date sendingDate) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.text = text;
        this.sendingDate = sendingDate;
    }

    public Contact getSender() {
        return sender;
    }

    public Contact getReceiver() {
        return receiver;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public Date getSendingDate() {
        return sendingDate;
    }
}
