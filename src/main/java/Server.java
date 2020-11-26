package java;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class java.Server
 *
 * @author Kamila Meshcheryakova
 * created by 26.11.2020
 */
public class Server {

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message message = connection.receive();
                if (message.getType() == MessageType.USER_NAME) {
                    String name = message.getData();
                    if (name != null && name.length() > 0 && !connectionMap.containsKey(name)) {
                        connectionMap.put(name, connection);
                        connection.send(new Message(MessageType.NAME_ACCEPTED));
                        return name;
                    }
                }
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            connectionMap.forEach((clientName, clientConnection) -> {
                try {
                    if (!clientName.equals(userName)) {
                        connection.send(new Message(MessageType.USER_ADDED, clientName));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) {
                    Message newMessage = new Message(MessageType.TEXT,
                            String.format("%s: %s", userName, message.getData()));
                    sendBroadcastMessage(newMessage);
                } else {
                    ConsoleHelper.writeMessage("The type of message is not a text");
                }
            }
        }

        public void run() {
            ConsoleHelper.writeMessage("The connection is established with socketAddress = " + socket.getRemoteSocketAddress());
            try (Connection connection = new Connection(socket)) {
                String clientName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, clientName));
                notifyUsers(connection, clientName);
                serverMainLoop(connection, clientName);
                connectionMap.remove(clientName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, clientName));
                ConsoleHelper.writeMessage("The connection is closed");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendBroadcastMessage(Message message) {
        if (connectionMap.isEmpty()) return;
        connectionMap.forEach((clientName, connection) -> {
            try {
                connection.send(message);
            } catch (IOException e) {
                try {
                    connection.send(new Message(MessageType.TEXT, "The message can't be send"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        int port = ConsoleHelper.readInt();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("ServerSocket starts");
            while (true) {
                new Handler(serverSocket.accept()).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

