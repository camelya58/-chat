package java.client;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class ClientGuiModel
 *
 * @author Kamila Meshcheryakova
 * created by 26.11.2020
 */
public class ClientGuiModel {

    private final Set<String> allUserNames = new LinkedHashSet<>();
    private String newMessage;

    public Set<String> getAllUserNames() {
        return Collections.unmodifiableSet(allUserNames);
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    public void addUser(String newUserName) {
        allUserNames.add(newUserName);
    }

    public void deleteUser(String newUserName) {
        allUserNames.remove(newUserName);
    }
}
