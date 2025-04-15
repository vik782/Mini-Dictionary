/**
 * Vincent Kurniawan
 *
 * ErrorExceptions.java: Information of error codes, description and message for a particular exception thrown
 *
 */

package Response;

import javax.swing.*;

public class ErrorExceptions {

    public static final String ARGUMENTS_LENGTH_ERROR = "400";
    public static final String SERVER_PORT_ERROR = "401";
    public static final String DICTIONARY_PARSE_ERROR = "402";
    public static final String DICTIONARY_PATH_ERROR = "403";
    public static final String BIND_PORT_ERROR = "404";
    public static final String INVALID_CLIENT_REQUEST = "405";
    public static final String QUEUE_WAIT_ERROR = "406";
    public static final String CLIENT_DISCONNECT = "407";
    public static final String INVALID_QUERY_TYPE = "408";
    public static final String THREAD_PROCESS_ERROR = "409";
    public static final String PARSE_REQUEST_ERROR = "410";
    public static final String CONNECT_REFUSED_ERROR = "411";
    public static final String UNKNOWN_HOST_ERROR = "412";
    public static final String SERVER_DISCONNECTED = "413";
    public static final String EMPTY_INPUT = "414";
    public static final String TOO_MANY_WORDS = "415";
    public static final String NON_ASCII = "416";

    public static void InvalidArguments () {
        String description = "Invalid Arguments";
        String message = "Invalid number of arguments, needs to exactly be 2: <port number> <dictionary path>";
        showError(ARGUMENTS_LENGTH_ERROR, description, message);
    }
    public static void serverPortError (Exception e) {
        String description = "Invalid Server Port";
        showError(SERVER_PORT_ERROR, description, e.getMessage());
    }
    public static void dictionaryParseError(Exception e){
        String description = "Dictionary Parse Error";
        showError(DICTIONARY_PARSE_ERROR, description, "Dictionary is not in proper JSON format");
    }

    public static void dictionaryPathError (Exception e){
        String description = "Invalid Dictionary Path";
        showError(DICTIONARY_PATH_ERROR, description, e.getMessage());
    }

    public static void bindError (Exception e){
        String description = "Port Bind Error";
        showError(BIND_PORT_ERROR, description, e.getMessage());
    }

    public static void clientInputError (Exception e){
        String description = "Invalid request format sent by client";
        showError(INVALID_CLIENT_REQUEST, description, e.getMessage());
    }

    public static void priorityBlockingQueueError (Exception e){
        String description = "Error in waiting for queue";
        showError(QUEUE_WAIT_ERROR, description, e.getMessage());
    }

    public static void threadProcessError (Exception e){
        String description = "Error in executing a thread";
        showError(THREAD_PROCESS_ERROR, description, e.getMessage());
    }

    public static void clientDisconnected (Exception e, int clientNo){
        String description = "Client - " + clientNo+ " has disconnected";
        notifyGUI(CLIENT_DISCONNECT, description, e.getMessage());
    }

    public static void unknownRequest (int clientNo){
        String description = "Client - " + clientNo+ " sent an unknown query";
        notifyGUI(INVALID_QUERY_TYPE, description, "Client query can't be processed by server");
    }

    public static void parseRequestError (Exception e, int clientNo){
        String description = "Parsing of request error by Client " + clientNo;
        notifyGUI(PARSE_REQUEST_ERROR, description, e.getMessage());
    }

    public static void hostError (Exception e){
        String description = "Unable to connect to specified host/domain name";
        showError(UNKNOWN_HOST_ERROR, description, e.getMessage());
    }

    public static void connectRefusedError (Exception e){
        String description = "Connection to server is refused";
        showError(CONNECT_REFUSED_ERROR, description, e.getMessage());
    }

    public static void serverDisconnected (Exception e){
        String description = "Server has disconnected or currently not alive";
        showError(SERVER_DISCONNECTED, description, e.getMessage());
    }

    public static void emptyInput (){
        String description = "Input is Empty";
        notifyGUI(EMPTY_INPUT, description, "Input cannot be null or empty");
    }

    public static void tooManyWords (){
        String description = "Invalid number of words";
        notifyGUI(TOO_MANY_WORDS, description, "Only 1 word can be queried at at time");
    }

    public static void nonASCII (){
        String description = "Invalid word format";
        notifyGUI(NON_ASCII, description, "Word should be in ASCII format");
    }

    public static String getError(String errorCode, String description, String errorContent) {
        return "Error Code -> " + errorCode + " (" + description + ")" +
                "\n" + "Message -> "+ errorContent;
    }
    private static void showError(String code, String description, String message) {
        JOptionPane.showConfirmDialog(
                null,
                getError(code,description, message),
                "Error",
                JOptionPane.DEFAULT_OPTION
        );
        System.exit(1);
    }

    private static void notifyGUI(String code, String description, String message) {
        JOptionPane.showConfirmDialog(
                null,
                getError(code,description, message),
                "Notification",
                JOptionPane.DEFAULT_OPTION
        );
    }

}
