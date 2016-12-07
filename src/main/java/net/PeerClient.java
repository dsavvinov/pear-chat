package net;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import models.Log;
import models.pojos.User;
import net.grpc.ChatServiceGrpc;
import net.grpc.ChatServiceOuterClass;

import java.util.concurrent.TimeUnit;

import static net.grpc.ChatServiceGrpc.*;

public class PeerClient {
    private final Log log;
    private final ManagedChannel channel;
    private final ChatServiceBlockingStub blockingStub;

    public PeerClient(String host, int port, Log log) {
        this.log = log;
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        blockingStub = ChatServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() {
        log.info("[PeerClient]: Shutting down");
        boolean isShuttedDown = false;

        try {
            isShuttedDown = channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) { }

        if (!isShuttedDown) {
            throw new RuntimeException("Can't shutdown client channel, terminating");
        }
        log.info("[PeerClient]: Shut down successfully");
    }

    public boolean sendMessage(User author, String text) {
        ChatServiceOuterClass.Message msg = ChatServiceOuterClass.Message.newBuilder()
                .setText(text)
                .setNick(author.name)
                .setAddress(author.address)
                .build();

        try {
            ChatServiceOuterClass.Response response = blockingStub.sendMessage(msg);
        } catch (StatusRuntimeException e) {
            log.error("Failed to send message: " + e.getMessage());
            return false;
        }
        log.info("[PeerClient]: Sent message successfully");
        return true;
    }
}
