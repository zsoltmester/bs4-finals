package hu.beesmarter.finalapp.communication;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The main class to communicate with the server.
 */
public class ServerCommunicator {

    private static final String TAG = ServerCommunicator.class.getSimpleName();

    private static final String ENCODING = "ISO-8859-1";

    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private String address;
    private int port;

    /**
     * Constructor of the class.
     *
     * @param address the ip address.
     * @param port    the port number.
     */
    public ServerCommunicator(@NonNull String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * Starts the communication (enable socket).
     */
    public void start() throws IOException {
        socket = new Socket(address, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        br = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
        outputStream = new BufferedOutputStream(outputStream);
        pw = new PrintWriter(new OutputStreamWriter(outputStream, ENCODING));
        Log.d(TAG, "Connection started!");
    }

    /**
     * Ends the communication (close the socket).
     */
    public void end() throws IOException {
        socket.close();
        Log.d(TAG, "Connection started!");
    }

    private void sendMessage(@NonNull String message) {
        pw.println(message);
        pw.flush();
        Log.d(TAG, "Message sent: " + message);
    }

    private @NonNull String getMessage() throws IOException {
        String message = br.readLine();
        if (message == null) {
            throw new IOException();
        }
        Log.d(TAG, "Message received: " + message);
        return message;
    }

    /**
     * Plays the 'hello server' part of the communication.
     */
    public void helloServer() throws IOException {
        String message = getMessage();
        if (message.equals(ServerMessages.COMM_MSG_HELLO_SERVER)) {
            sendMessage(ServerMessages.COMM_MSG_HELLO_CLIENT);
        } else {
            Log.d(TAG, "Something shit happened. We waited: "
                    + ServerMessages.COMM_MSG_HELLO_SERVER + ", but we got: " + message);
        }

        message = getMessage();
        if (message.equals(ServerMessages.COMM_MSG_REQUEST_ASK_ID)) {
            sendMessage(ServerMessages.COMM_MSG_REPLY_ASK_ID);
        } else {
            Log.d(TAG, "Something shit happened. We waited: "
                    + ServerMessages.COMM_MSG_REQUEST_ASK_ID + ", but we got: " + message);
        }
    }
}


