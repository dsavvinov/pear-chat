package net;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import models.Chat;
import models.Log;
import models.UsersDB;
import net.grpc.ChatService;

import java.io.IOException;

/**
 * Server for incoming messages based on gRPC.
 * It just starts and shuts down gRPC server.
 *
 * See ChatService for logic on receiving message.
 */
public class PeerServer {
    private final Server server;
    private final Log log;
    private final UsersDB usersDB;

    public PeerServer(int port, Chat chat, Log log, UsersDB usersDB) {
        this.log = log;
        this.usersDB = usersDB;
        server = ServerBuilder.forPort(port).addService(new ChatService(chat, this.usersDB, log)).build();
    }

    public void start() throws IOException {
        server.start();
        log.info("[PeerServer] Started peer server on port <" + server.getPort() + ">, listening for connections...");
    }

    public void shutdown() {
        if (server != null) {
            server.shutdownNow();
            while (!server.isTerminated()) {
                try {
                    server.awaitTermination();
                } catch (InterruptedException ignored) { }
            }
        }
    }
}
