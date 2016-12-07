package net;

import models.*;
import models.pojos.User;

import java.io.IOException;

public class NetAgent implements Agent {
    private final Log log;
    private final UsersDB usersDB;
    private PeerServer peerServer;
    private PeerClient peerClient = null;

    public NetAgent(int port, Log log, Chat chat, UsersDB usersDB) throws IOException {
        this.log = log;
        this.usersDB = usersDB;
        peerServer = new PeerServer(port, chat, log, this.usersDB);
        peerServer.start();
    }

    public void connect(String address) {
        int clientPort = Integer.parseInt(address.substring(address.lastIndexOf(':') + 1, address.length()));
        String host = address.substring(0, address.lastIndexOf(':'));
        log.info("[Agent]: Connecting to client on host<" + host + ">, port: " + clientPort);
        peerClient = new PeerClient(host, clientPort, log);
    }

    @Override
    public boolean sendMessage(User author, User adressee, String message) {
        connect(adressee.address);
        if (peerClient == null) {
            log.error("Error: connect first!");
            return false;
        }

        log.info("[Agent]: Sending message");
        boolean result = peerClient.sendMessage(author, message);
        disconnect();

        return result;
    }

    public void disconnect() {
        peerClient.shutdown();
        log.info("[Agent]: Shutting down connection");
        peerClient = null;
    }

    @Override
    public void shutdown() {
        if (peerClient != null) {
            peerClient.shutdown();
        }

        peerServer.shutdown();
    }
}
