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

    public void get(String key) {
        Request request = new Request("get", key);
        Response response =  sendRequest(request);
        if (!Objects.equals(response.getMessage(), "")) {
            System.out.println("Response: " + response.getMessage());
        } else {
            System.out.println("Error: " + response.getError());
        }
    }

    public void set(String key, String value) {
        Request request = new Request("set", key, value);
        Response response = sendRequest(request);
        if (!Objects.equals(response.getMessage(), "")) {
            System.out.println("Response: " + response.getMessage());
        } else {
            System.out.println("Error: " + response.getError());
        }
    }

    public void put(String key, String value) {
        Request request = new Request("put", key, value);
        Response response = sendRequest(request);

        if (!Objects.equals(response.getMessage(), "")) {
            System.out.println("Response: " + response.getMessage());
        } else {
            System.out.println("Error: " + response.getError());
        }
    }

    public Socket getSocket(){
        return socket;
    }

    public static void main(String[] args) throws IOException {
        ServerClient client = new ServerClient("pie.lynchburg.edu", 50701);

        Scanner scan = new Scanner(System.in);
        PrintStream out = new PrintStream(System.out);

        //initial set
        client.set("one", "A");

        //get on initial set
        client.get("one");

        //replacing the initial set with a new value
        client.put("one", "B");

        //gets the new value from put
        client.get("one");

        //attempts to set the same value
        client.set("one", "A");

        //attempts to change value of a key which is not present
        client.put("two", "B");

        //attempts to get value from a key not in the map
        client.get("two");
    }
}