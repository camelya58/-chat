package java.client;

import java.ConsoleHelper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class BotClient
 *
 * @author Kamila Meshcheryakova
 * created by 26.11.2020
 */
public class BotClient extends Client {

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return "date_bot_" + (int) (Math.random() * 100);
    }

    public class BotSocketThread extends Client.SocketThread {

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            if (message == null || !message.contains(": ")) return;
            String userName = message.split(": ")[0];
            String textMessage = message.split(": ")[1];
            SimpleDateFormat sdf = new SimpleDateFormat("d.MM.YYYY");
            SimpleDateFormat monthFormat = new SimpleDateFormat("d.MMMM.YYYY");
            SimpleDateFormat timeFormat = new SimpleDateFormat("d.MMMM.YYYY H:mm:ss");
            Date currentDate = Calendar.getInstance().getTime();
            String request = "";
            switch (textMessage) {
                case "дата":
                    request = sdf.format(currentDate);
                    break;
                case "день":
                    String day = sdf.format(currentDate);
                    request = day.substring(0, day.indexOf("."));
                    break;
                case "месяц":
                    String date = monthFormat.format(currentDate);
                    request = date.substring(date.indexOf(".") + 1, date.lastIndexOf("."));
                    break;
                case "год":
                    String year = sdf.format(currentDate);
                    request = year.substring(year.lastIndexOf(".") + 1);
                    break;
                case "время":
                    String time = timeFormat.format(currentDate);
                    request = time.substring(time.indexOf(" ") + 1);
                    break;
                case "час":
                    String hour = timeFormat.format(currentDate);
                    request = hour.substring(hour.indexOf(" ") + 1, hour.indexOf(":"));
                    break;
                case "минуты":
                    String minutes = timeFormat.format(currentDate);
                    request = minutes.substring(minutes.indexOf(":") + 1, minutes.lastIndexOf(":"));
                    break;
                case "секунды":
                    String seconds = timeFormat.format(currentDate);
                    request = seconds.substring(seconds.lastIndexOf(":") + 1);
                    break;
                default:
                    return;
            }
            sendTextMessage(String.format("Информация для %s: %s", userName, request));
        }
    }

    public static void main(String[] args) {
        new BotClient().run();
    }
}
