/**
 * Vincent Kurniawan
 *
 * DictionaryClient.java: The client program to interact with the server
 *
 */
package Client;

import GUI.GUIClient;
import Response.ErrorExceptions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;


public class DictionaryClient {

    private static int serverPort;
    private static String serverAddress;

    private static void verifyInput(String [] args){

        if (args.length < 1 || args.length > 2) {
            ErrorExceptions.InvalidArguments();
        }
        try {
            serverPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            ErrorExceptions.serverPortError(e);
        }
        serverAddress = args[0];
    }

    public static void main (String[] args)  {
        verifyInput(args);
        Socket socket = null;

        try {
            socket = new Socket(serverAddress, serverPort);

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            Middleware clientAPI = new Middleware(socket, input, output);

            GUIClient clientGUI = new GUIClient("Vik's Dictionary Client", clientAPI);

        }

        catch (ConnectException e){
            ErrorExceptions.connectRefusedError(e);
        }
        catch (UnknownHostException e) {
            ErrorExceptions.hostError(e);
        }
        catch (IOException e){
            ErrorExceptions.clientInputError(e);
        }

    }

}
