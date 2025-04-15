/**
 * Vincent Kurniawan
 *
 * DictionaryThread.java: Thread to execute dictionary operations stored in thread-pool
 *
 */

package Server;

import Dictionary.CustomDictionary;
import Dictionary.DictionaryFunctions;
import Response.DuplicateWord;
import Response.ErrorExceptions;
import Response.InvalidSearch;
import Response.ResponseCodes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;


public class DictionaryThread implements Runnable, Comparable<DictionaryThread> {

    private static int totalClients = 0;

    private static String latestRequest = "No requests yet!";
    private Date timeAtQueue;

    private Socket client;

    private int clientNo;

    private DataInputStream input;
    private DataOutputStream output;
    private DictionaryFunctions dictionary;
    private JSONParser parser;

    public DictionaryThread(Socket client, int clientNo,  Date timeAtQueue, CustomDictionary dictionary){
        this.timeAtQueue = timeAtQueue;
        this.client = client;
        this.clientNo = clientNo;
        this.dictionary = dictionary;
        this.parser = new JSONParser();

        // each time a dictionary thread is made, a new client is connected
        totalClients++;
    }

    @Override
    public void run()  {
            try {
                // create input and output channels of from server to client
                input = new DataInputStream(client.getInputStream());
                output = new DataOutputStream(client.getOutputStream());

                // constantly awaits for client requests
                while (true) {

                    // attempt to receive client request
                        String clientMsg = null;
                        try {
                            clientMsg = input.readUTF();
                        } catch (SocketException e) {
                            // Client disconnected with server
                            totalClients--;
                            ErrorExceptions.clientDisconnected(e, clientNo);
                            break;
                        }

                        // parses client request
                        String request = parseClientMessage(clientMsg);
                        latestRequest = clientMsg;

                        switch (request) {
                            case ResponseCodes.SEARCH_QUERY:
                                // search request
                                search(clientMsg);
                                break;
                            case ResponseCodes.CHECK_ADD:
                                // checks add request
                                checkAdd(clientMsg);
                                break;
                            case ResponseCodes.ADD_QUERY:
                                // add request
                                add(clientMsg);
                                break;
                            case ResponseCodes.REMOVE_QUERY:
                                // remove request
                                remove(clientMsg);
                                break;
                            case ResponseCodes.CHECK_UPDATE:
                                // check update request
                                checkUpdate(clientMsg);
                                break;
                            case ResponseCodes.UPDATE_QUERY:
                                // update request
                                update(clientMsg);
                                break;
                            default:
                                // unknown client request type
                                ErrorExceptions.unknownRequest(clientNo);
                                output.writeUTF("Unknown Request Command by Client " + clientNo + "; Client Request: " + clientMsg);
                                output.flush();
                                break;
                        }
                }
            }  catch (IOException e){
                ErrorExceptions.threadProcessError(e);
            }  finally {
                try {
                    input.close();
                    output.close();
                    client.close();
                    System.out.println("Client - " + clientNo+ " has disconnected");
                } catch (IOException e) {
                    ErrorExceptions.threadProcessError(e);
                }
            }
    }

    public String parseClientMessage(String request) {
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e){
            ErrorExceptions.parseRequestError(e, clientNo);
        }
        String code = req.get("QUERY_TYPE").toString();
        return code;
    }
    public void search(String request) throws IOException{
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e){
            ErrorExceptions.parseRequestError(e, clientNo);
        }
        String keyword = req.get("KEYWORD").toString();
        String value;
        String response;
        try {
            value = dictionary.search(keyword);
            value = value.replaceAll("\\[|\\]", "");
            value = value.replaceAll("\"", "");
            response = keyword + ": " + value;
        }
        catch (InvalidSearch e) {
            response = (e.errorMessage(keyword));
        }
        output.writeUTF(response);
        output.flush();
    };

    public void checkAdd(String request) throws IOException{
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e){
            ErrorExceptions.parseRequestError(e, clientNo);
        }
        String keyword = req.get("KEYWORD").toString();
        String response;
        try {
            dictionary.search(keyword);
            response = keyword + ": " + ResponseCodes.DUPLICATE;
        }
        catch (InvalidSearch e) {
            response = (e.getMessage());
        }
        output.writeUTF(response);
        output.flush();
    };

    public void checkUpdate(String request) throws IOException{
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e){
            ErrorExceptions.parseRequestError(e, clientNo);
        }
        String keyword = req.get("KEYWORD").toString();
        String response;
        try {
            dictionary.search(keyword);
            response = keyword + ": " + ResponseCodes.DUPLICATE;
        }
        catch (InvalidSearch e) {
            response = (e.getMessage());
        }
        output.writeUTF(response);
        output.flush();
    };
    public void remove(String request) throws IOException{
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e){
            ErrorExceptions.parseRequestError(e, clientNo);
        }
        String keyword = req.get("KEYWORD").toString();
        String response;

        try {
            dictionary.remove(keyword);
            response = keyword + " successfully removed!";
        } catch (InvalidSearch e) {
            response = (e.errorMessage(keyword));
        }
        output.writeUTF(response);
        output.flush();
    };

    public void add(String request) throws IOException{
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e){
            ErrorExceptions.parseRequestError(e, clientNo);
        }
        String keyword = req.get("KEYWORD").toString();
        String meaning = req.get("MEANING").toString();
        String response;
        try {
            dictionary.add(keyword, meaning);
            meaning = meaning.replaceAll("\\[|\\]", "");
            meaning = meaning.replaceAll("\n", ", ");
            meaning = meaning.substring(0, meaning.length() - 2);
            response = keyword + " successfully added with: " + meaning;

        }  catch (DuplicateWord e){
            response = (e.errorMessage(keyword));
        }

        output.writeUTF(response);
        output.flush();
    };

    public void update(String request) throws IOException{
        JSONObject req = new JSONObject();
        try {
            req = (JSONObject) parser.parse(request);
        } catch (ParseException e){
            ErrorExceptions.parseRequestError(e, clientNo);
        }
        String keyword = req.get("KEYWORD").toString();
        String meaning = req.get("MEANING").toString();
        String response;
        try {

            dictionary.update(keyword, meaning);
            meaning = meaning.replaceAll("\\[|\\]", "");
            meaning = meaning.replaceAll("\n", ", ");
            meaning = meaning.substring(0, meaning.length() - 2);
            response = keyword + " successfully updated with: " + meaning;

        }  catch (InvalidSearch e){
            response = (e.errorMessage(keyword));
        }

        output.writeUTF(response);
        output.flush();
    };

    public int compareTo(DictionaryThread other) {
        // first-come-first-serve queue
        if (this.timeAtQueue.before(other.timeAtQueue)){
            return 1;
        }
        else {
            return -1;
        }
    }

    public static int getTotalClients(){
        return totalClients;
    }
    public static String getLatestRequest(){
        return latestRequest;
    }
}
