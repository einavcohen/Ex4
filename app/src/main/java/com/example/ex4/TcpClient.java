package com.example.ex4;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * TcpClient client class
 */
public class TcpClient {
    private String ip;
    private int port;
    private Socket clientSocket = null;
    private Boolean isConnectedToSimulator = false;
    private OutputStream stream;
    private PrintWriter writer;
    private static TcpClient instance = null;

    /**
     * an empty constructor
     */
    public TcpClient() {
    }

    /**
     * getInstance function- create a new instance of TcpClient
     * @return TcpClient
     */
    public static TcpClient getInstance() {
        if(instance == null)
            instance = new TcpClient();
        return instance;
    }

    /**
     * setIpAndPort function- set the connection values to the server
     * @param ip ip val
     * @param port port val
     */
    public void setIpAndPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Connect function- create the connection between the server and
     * the client
     */
    public void Connect() {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    InetAddress serverAddress = InetAddress.getByName(ip);
                    clientSocket = new Socket(serverAddress, port);
                    stream = clientSocket.getOutputStream();
                    writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    Log.e("TCP", "S: Granted");
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Send function- sent messages to the server and prints them
     * send the commands to the simulator
     * @param command string of set command
     */
    public void Send(final String command)
    {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    if (clientSocket != null && writer != null) {
                        writer.println(command);
                        writer.flush();
                    }
                }
                catch(Exception e) {
                    Log.e("TCP", "S: Error", e);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     *  close function- close the connection with the server
     *  */
    public void close() {
        try {
            if(clientSocket != null) {
                clientSocket.close();
                isConnectedToSimulator = false;
            } else {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}