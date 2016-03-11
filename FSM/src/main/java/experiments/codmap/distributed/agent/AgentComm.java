package experiments.codmap.distributed.agent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import experiments.codmap.distributed.Message;
import experiments.codmap.distributed.Message.Command;

public class AgentComm {
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    public AgentComm(String brokerIp, int brokerPort) throws Exception {
        for (int i = 0; i<10 ; i++) {
            try {
                socket = new Socket(brokerIp, brokerPort);
                break;
            } catch (ConnectException e) {
                System.err.println("Connection error, waiting...");
                Thread.sleep(1000);
            }
        }
        if (socket == null) {
            throw new AssertionError("Cannot connect to server");
        }
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream( socket.getInputStream());       
    }

    synchronized public Object sendAndReceiveAnswer(Message message) {
        Message receivedMessage = (Message) sendAndReceiveAny(message);
        if (!receivedMessage.command.equals(message.command) && !receivedMessage.command.equals(Command.OK)) {
            throw new AssertionError("Unexpected message: " + receivedMessage.command + " expected: " + message.command);
        }
        return receivedMessage.content;
    }

    public Message sendAndReceiveAny(Message message) {
        send(message);
        return receive();
    }

    public void send(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message receive() {
        try {
            Message receivedMessage = (Message) in.readObject();
            return receivedMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    synchronized public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}

