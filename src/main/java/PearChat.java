import controllers.ChatController;
import controllers.NetController;
import controllers.UsersController;
import models.*;
import models.implementations.ChatImpl;
import models.implementations.LogImpl;
import models.implementations.UsersDBImpl;
import models.pojos.User;
import net.Agent;
import net.NetAgent;
import ui.MainWindow;

import java.io.IOException;

public class PearChat {

    public static void main(String args[]) {
        int port = Integer.parseInt(args[0]);
        User.THIS_USER = new User(args[1], "localhost:" + port);

        Log log = new LogImpl();
        Chat chat = new ChatImpl(log);
        UsersDB usersDB = new UsersDBImpl(log);
        Agent netAgent;
        try {
            netAgent = new NetAgent(port, log, chat, usersDB);
        } catch (IOException e) {
            System.err.println("Can't create NetAgent, shutting down: " + e.toString());
            return;
        }

        MainWindow mainWindow = new MainWindow(log);

        NetController.setAgent(netAgent);
        NetController.setLog(log);
        ChatController.setChat(chat);
        ChatController.setLog(log);
        UsersController.setUsersDB(usersDB);
        UsersController.setLog(log);

        log.addObserver(mainWindow);
        chat.addObserver(mainWindow);
        usersDB.addObserver(mainWindow);

        java.awt.EventQueue.invokeLater(() -> mainWindow.setVisible(true));

        Runtime.getRuntime().addShutdownHook(new Thread(netAgent::shutdown));
    }
}
