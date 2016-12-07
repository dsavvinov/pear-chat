# Pear chat

## Installation and launching

```
  $ gradle installDist
  $ cd build/install/PearChat/bin
  $ ./PearChat <port of peer-server> <nickname>
```

## Usage

1. Enter address of peer in field "Enter IP here" and assigned to this
peer name in "Nick" field.
2. Click "Connect" button. This peer will be remembered for further use.
3. Choose desired adresse of message in the combobox right to "Chat with" label.
4. Type message and hit "Send" button.

*Note*. Any peer that have started conversation with you will appear in the list of
peers with her/his declared name. **However**, if you assigned some name to address,
then all messages from this address will always appear by that name, independent
of the name that the peer has chosen.

## Design note

Design is very straight-forward.

We use MVC pattern with View that listens for changes in the Model. View is built using Java Swing library and some GUI Builders.

For networking, we use gRPC. `ChatService` class is a end-point of gRPC machinery and implements user-defined logic of receiving messages.  
Generated Protobuf artifacts are committed to the repository because I'm too lazy to configure build properly.
