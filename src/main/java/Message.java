package java;

import java.io.Serializable;

/**
 * Class java.Message
 *
 * @author Kamila Meshcheryakova
 * created by 26.11.2020
 */
public class Message implements Serializable {

    private final MessageType type;
    private final String data;

    public Message(MessageType type) {
        this.type = type;
        this.data = null;
    }

    public Message(MessageType type, String data) {
        this.type = type;
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
