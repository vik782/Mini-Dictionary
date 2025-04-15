/**
 * Vincent Kurniawan
 *
 * DictionaryServer.java: The server program to provide functional operations of dictionary to client
 *
 */

package Server;

import GUI.GUIServer;
import Response.ErrorExceptions;
import Dictionary.CustomDictionary;

import java.io.IOException;
import java.net.*;
import javax.net.*;
import java.util.*;

public class DictionaryServer {

    private static int serverPort;
    private static String dictionaryPath;

    private static Socket socket;
    private static ThreadPool threadPool;

    private static int clientNo;

    private static void verifyInput(String [] args){

        if (args.length < 1 || args.length > 2) {
            ErrorExceptions.InvalidArguments();
        }
        try {
            serverPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e){
            ErrorExceptions.serverPortError(e);
        }

        dictionaryPath = args[1];
    }
    public static void main (String[] args) throws IOException {

        // checks input arguments (server port and dictionary path)
        verifyInput(args);

        // generate dictionary
        CustomDictionary dictionary = new CustomDictionary(dictionaryPath);

        // generate new thread-pool
        threadPool = new ThreadPool();

        // generate GUI for server
        DictionaryServer dictionaryServer= new DictionaryServer();
        GUIServer serverGUI = new GUIServer("Vik's Dictionary Server");

        try {
            // generate socket for server
            ServerSocketFactory serverFactory = ServerSocketFactory.getDefault();
            ServerSocket serverSocket = serverFactory.createServerSocket(serverPort);
            serverSocket.setReuseAddress(true);

            while (true) {
                // a client is connected to the server
                socket = serverSocket.accept();

                // assign unique identifer for each client connected
                clientNo++;
                System.out.println("comp90015/Client " + clientNo + " just connected with server!");

                // assign time at which client connected
                Date currentTime = new Date();

                // task from client request
                DictionaryThread dictionaryThread = new DictionaryThread(socket, clientNo, currentTime, dictionary);

                // execute task with thread-pool of worker threads
                threadPool.execute(dictionaryThread);
            }
        } catch (BindException e) {
            ErrorExceptions.bindError(e);
        } catch (IOException e) {
            ErrorExceptions.clientInputError(e);
        } finally {
            // shuts down running thread-pool
            socket.close();
            threadPool.shutdown();
        }
    }
}
