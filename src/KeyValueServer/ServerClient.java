package KeyValueServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class ServerClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ServerClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public Response sendRequest(Request request) {
        try {
            out.writeObject(request);
            return (Response) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response get(String key) {
        Request request = new Request("get", key);
        return sendRequest(request);
    }

    public Response set(String key, String value) {
        Request request = new Request("set", key, value);
        return sendRequest(request);
    }

    public Response put(String key, String value) {
        Request request = new Request("put", key, value);
        return sendRequest(request);
    }

    public Socket getSocket(){
        return socket;
    }

    public static void main(String[] args) throws IOException {
        ServerClient client = new ServerClient("localhost", 50701);

        Scanner scan = new Scanner(System.in);
        PrintStream out = new PrintStream(System.out);

        //initial set
        Response response = client.set("one", "A");
        if (!Objects.equals(response.getMessage(), "")) {
            out.println("Response: " + response.getMessage());
        } else {
            out.println("Error: " + response.getError());
        }

        //get on initial set
        response = client.get("one");
        if (!Objects.equals(response.getMessage(), "")) {
            out.println("Response: " + response.getMessage());
        } else {
            out.println("Error: " + response.getError());
        }

        //replacing the initial set with a new value
        response = client.put("one", "B");
        if (!Objects.equals(response.getMessage(), "")) {
            out.println("Response: " + response.getMessage());
        } else {
            out.println("Error: " + response.getError());
        }

        //gets the new value from put
        response = client.get("one");
        if (!Objects.equals(response.getMessage(), "")) {
            out.println("Response: " + response.getMessage());
        } else {
            out.println("Error: " + response.getError());
        }

        //attempts to set the same value
       response = client.set("one", "A");
        if (!Objects.equals(response.getMessage(), "")) {
            out.println("Response: " + response.getMessage());
        } else {
            out.println("Error: " + response.getError());
        }

        //attempts to change value of a key which is not present
        response = client.put("two", "B");
        if (!Objects.equals(response.getMessage(), "")) {
            out.println("Response: " + response.getMessage());
        } else {
            out.println("Error: " + response.getError());
        }

        //attempts to get value from a key not in the map
        response = client.get("two");
        if (!Objects.equals(response.getMessage(), "")) {
            out.println("Response: " + response.getMessage());
        } else {
            out.println("Error: " + response.getError());
        }
    }
}