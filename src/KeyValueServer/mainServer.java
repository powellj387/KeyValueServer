package KeyValueServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class mainServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Map<String, String> keyValueMap = new HashMap<>();

        // Create the server socket to accept connections
        ServerSocket serverSocket = new ServerSocket(50701);

        while (true) {
            System.out.println("Allowing connections");
            Socket aSocket = serverSocket.accept();

            ObjectOutputStream out = new ObjectOutputStream(aSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(aSocket.getInputStream());

            try {
                while (true) {
                    Object request = in.readObject();
                    Request aRequest = (Request) request;
                    Response aResponse = new Response("","","");

                    switch (aRequest.getMethod()) {
                        case "get":
                            String key = aRequest.getKey();
                            if (keyValueMap.containsKey(key)) {
                                 aResponse.setMessage(keyValueMap.get(key));
                            } else {
                                aResponse.setError("Key not found");
                            }
                            break;

                        case "set":
                            key = aRequest.getKey();
                            String value = aRequest.getValue();
                            if (key == null || key.isEmpty()) {
                                aResponse.setError("Key cannot be empty or null");
                            } else if (keyValueMap.containsKey(key)) {
                                aResponse.setError("Key already exists");
                            } else {
                                keyValueMap.put(key, value);
                                aResponse.setMessage("Key-value pair added successfully");
                            }
                            break;

                        case "put":
                            key = aRequest.getKey();
                            value = aRequest.getValue();
                            if (keyValueMap.containsKey(key)) {
                                keyValueMap.put(key, value);
                                aResponse.setMessage("Key-value pair updated successfully");
                            } else {
                                aResponse.setError("Key not found");
                            }
                            break;
                        case "exit":
                            // Optionally, you can add an "exit" command to close the connection
                            out.close();
                            in.close();
                            aSocket.close();
                            return; // Exit the loop
                        default:
                            aResponse.setError("Invalid method");
                            break;
                    }
                    out.writeObject(aResponse);
                }
            } catch (IOException e) {
                // Handle the IOException that occurs when the client disconnects
                e.printStackTrace();
            }
        }
    }
}