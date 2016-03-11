package experiments.codmap.distributed.broker;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import experiments.codmap.distributed.Message;
import experiments.codmap.distributed.Message.Command;

public class BrokerComm {

    protected Socket agentSocket;

    ObjectOutputStream out;
    ObjectInputStream in;
    
    String agentName;

    private InputStream inStream;

    public BrokerComm(Socket agentSoc) {
        agentSocket = agentSoc;
    }

    public void connect() {
        System.out.println("New Communication Thread Started: " + agentSocket.getPort() + " -- " + agentSocket.getLocalPort());

        try {
            out = new ObjectOutputStream(agentSocket.getOutputStream());
            inStream = agentSocket.getInputStream();
            in = new ObjectInputStream( inStream);
            agentName = (String) receiveMessage(Command.LOGIN);
            System.out.println("Agent logged in: " + agentName);
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
            System.exit(1);
        }
    }

    public void close() throws IOException {
        out.close();
        in.close();
        agentSocket.close();
    }

    public Object receiveMessage(Command command) {
        Message message = (Message) receiveMessage();
        if (!message.command.equals(command)) {
            throw new IllegalStateException("Unexpected command (" + message.command + ") expected (" + command + ")");
        }
        return message.content;
    }
    
    public Message receiveMessage() {
        try {
            return (Message) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAvailable() {
        try {
            return inStream.available() > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
