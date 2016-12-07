package net.grpc;

import io.grpc.stub.StreamObserver;
import models.*;
import models.pojos.User;

import java.util.Date;

public class ChatService extends ChatServiceGrpc.ChatServiceImplBase {
    private final Chat chat;
    private final UsersDB usersDB;
    private Log log;

    public ChatService(Chat chat, UsersDB usersDB, Log log) {
        this.chat = chat;
        this.usersDB = usersDB;
        this.log = log;
    }

    @Override
    public void sendMessage(ChatServiceOuterClass.Message request, StreamObserver<ChatServiceOuterClass.Response> responseObserver) {
        log.info("[ChatService]: Receiving message");
        usersDB.addUser(request.getNick(), request.getAddress());
        User user = usersDB.getUser(request.getNick());

        chat.addMessage(user, new Date(), request.getText());

        responseObserver.onNext(ChatServiceOuterClass.Response.newBuilder().build());
        responseObserver.onCompleted();
    }
}