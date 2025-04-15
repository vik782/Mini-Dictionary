/**
 * Vincent Kurniawan
 *
 * Middleware.java: The middleware program that acts as a communication API between client and server
 *
 */
package Client;

import Response.ErrorExceptions;
import Response.ResponseCodes;
import org.json.simple.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;


public class Middleware {

    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;

    public Middleware(Socket clientSocket, DataInputStream in, DataOutputStream out){
        this.clientSocket = clientSocket;
        this.in = new DataInputStream(in);
        this.out = new DataOutputStream(out);
    }

    private void closeSocket() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }

    public void processRequest(JSONObject request, JTextArea resLabel) throws IOException {
        resLabel.setText("");
        try {
            clientToServer(request);
            String serverResponse = serverToClient();
            resLabel.append(serverResponse);
            resLabel.update(resLabel.getGraphics());
        } catch (IOException e){
            ErrorExceptions.serverDisconnected(e);
            closeSocket();
        }
    }


    private boolean checkInputRequest (String request, JTextArea resLabel)  {
        resLabel.setText("");

            if (request.isEmpty()) {
                // resLabel.append(ResponseCodes.EMPTY_INPUT);
                ErrorExceptions.emptyInput();
                return false;
            } else {
                String[] inputs = request.split(" ");
                if (inputs.length > 1) {
                    // resLabel.append(ResponseCodes.TOO_MANY_WORDS);
                    ErrorExceptions.tooManyWords();
                    return false;
                } else if (!StringUtils.isAsciiPrintable(request)) {
                    // resLabel.append(ResponseCodes.NON_ASCII_CHARS);
                    ErrorExceptions.nonASCII();
                    return false;
                } else {
                    return true;
                }
            }
        }


    public void checkSearchRequest (String request, JTextArea resLabel) throws IOException  {
        resLabel.setText("");
        try {
            if (checkInputRequest(request, resLabel)) {
                String code = ResponseCodes.SEARCH_QUERY;
                JSONObject searchReq = new JSONObject();
                searchReq.put("QUERY_TYPE", code);
                searchReq.put("KEYWORD", request);
                processRequest(searchReq, resLabel);
            }
        } catch (IOException e){
            ErrorExceptions.serverDisconnected(e);
            closeSocket();
        }
    }

    public void checkRemoveRequest (String request, JTextArea resLabel) throws IOException  {
        resLabel.setText("");
        try {
            if (checkInputRequest(request, resLabel)) {
                String code = ResponseCodes.REMOVE_QUERY;
                JSONObject removeReq = new JSONObject();
                removeReq.put("QUERY_TYPE", code);
                removeReq.put("KEYWORD", request);
                processRequest(removeReq, resLabel);
            }
        } catch (IOException e){
            ErrorExceptions.serverDisconnected(e);
            closeSocket();
        }

    }

    public boolean validateAddRequest(String request, JTextArea resLabel) throws IOException  {
        resLabel.setText("");
        try {
            if (checkInputRequest(request, resLabel)) {
                String code = ResponseCodes.CHECK_ADD;
                JSONObject addReq = new JSONObject();
                addReq.put("QUERY_TYPE", code);
                addReq.put("KEYWORD", request);
                if (processAddRequest(addReq, resLabel)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (IOException e) {
            ErrorExceptions.serverDisconnected(e);
            closeSocket();
        }
        return false;
    }

    public boolean validateUpdateRequest(String request, JTextArea resLabel) throws IOException{
        resLabel.setText("");
        try {
            if (checkInputRequest(request, resLabel)) {
                String code = ResponseCodes.CHECK_UPDATE;
                JSONObject updateReq = new JSONObject();
                updateReq.put("QUERY_TYPE", code);
                updateReq.put("KEYWORD", request);
                if (processUpdateRequest(updateReq, resLabel)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (IOException e){
            ErrorExceptions.serverDisconnected(e);
            closeSocket();
        }
        return false;
    }

    public boolean processAddRequest(JSONObject request, JTextArea resLabel) throws IOException  {

        resLabel.setText("");
        try {
            clientToServer(request);
            String serverResponse = serverToClient();
            if (serverResponse.equals(ResponseCodes.NON_EXISTENT)) {
                return true;

            } else {
                resLabel.append(serverResponse);
                resLabel.update(resLabel.getGraphics());
                return false;
            }
        } catch (IOException e){
            ErrorExceptions.serverDisconnected(e);
            closeSocket();
        }
        return false;
    }

    public boolean processUpdateRequest(JSONObject request, JTextArea resLabel) throws IOException {
        resLabel.setText("");
        try {
            clientToServer(request);
            String serverResponse = serverToClient();
            if (serverResponse.equals(ResponseCodes.NON_EXISTENT)) {
                resLabel.append(serverResponse);
                resLabel.update(resLabel.getGraphics());
                return false;
            } else {
                return true;
            }
        } catch (IOException e){
            ErrorExceptions.serverDisconnected(e);
            closeSocket();
        }
        return false;
    }

    public void checkAddRequest (String keyword, String meanings, String code, JTextArea resLabel) throws IOException {
        try {
            JSONObject addReq = new JSONObject();
            addReq.put("QUERY_TYPE", code);
            addReq.put("KEYWORD", keyword);
            addReq.put("MEANING", meanings);
            processRequest(addReq, resLabel);
        } catch (IOException e){
            ErrorExceptions.serverDisconnected(e);
            closeSocket();
        }
    }
    public String serverToClient() throws IOException {
        String response = null;
        try {
            response = in.readUTF();
        } catch (IOException e){
            ErrorExceptions.serverDisconnected(e);
            closeSocket();
        }
        return response;
    }

    public void clientToServer(JSONObject request) throws IOException{
        out.writeUTF(request.toJSONString());
        out.flush();
    }


}
