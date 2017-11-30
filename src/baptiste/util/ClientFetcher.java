/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baptiste.util;

import baptiste.bean.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientFetcher {

    private String host;
    private int port;
    private Socket socket;
    private OutputStream outStream;
    private ArrayList<String> listObject;
    private boolean isConnceted = false;
    private String resourceName;
    private InputStream inputStreamFromServer;
    private OutputStream outputStreamFromServer;
    private Timer timer;
    private boolean isRunning;

    /**
     *
     * @param host
     * @param port
     * @param outStream
     */
    public ClientFetcher(String host, int port, OutputStream outStream) {

        this.host = host;
        this.port = port;
        this.outStream = outStream;
        initConnection();
        initIncommingDataListener();

    }

    /**
     *
     */
    private void initConnection() {
        try {

            this.socket = new Socket(host, port);
            this.inputStreamFromServer = this.socket.getInputStream();
            this.outputStreamFromServer = this.socket.getOutputStream();
            this.isConnceted = true;
            initTimer();
            this.isRunning = true;

        } catch (IOException ex) {
            this.isConnceted = false;
        }
    }

    /**
     *
     */
    private void initIncommingDataListener() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Thread.currentThread().setName("InputStream from : " + host);
                while (!socket.isClosed() && isConnceted) {

                    try {

                        DataInputStream fluxIn = new DataInputStream(inputStreamFromServer);

                        while (fluxIn.available() == 0) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {

                            }
                        }
                        initTimer();
                        byte[] lecteur = new byte[fluxIn.available()];
                        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                        while (fluxIn.available() > 0) {
                            fluxIn.read(lecteur);
                            byteStream.write(lecteur);
                            outStream.write(lecteur);
                            outStream.flush();

                        }

                        if (byteStream.size() > 0) {
                           //todo
                        }
                    } catch (IOException ex) {
                        break;
                    }
                }
            }

        }).start();
    }

    /**
     *
     */
    private void initTimer() {
        if (this.timer == null) {
            this.timer = new Timer("KAL Timer with : " + host);
        }
        if (isRunning) {
            timer.cancel();
            this.timer = new Timer("KAL Timer with : " + host);
        }
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                try {
                    socket.close();
                    isConnceted = false;
                    outStream.close();
                    timer.cancel();
                } catch (IOException ex) {

                }
            }
        };
        timer.schedule(timerTask, 1000 * 60);
    }

    /**
     *
     * @param request
     */
    public void fetchObject(String request) {
        initTimer();
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    if (isConnceted) {
                        DataOutputStream dataOutputStream = new DataOutputStream(outputStreamFromServer);
                        HttpRequest r = HttpRequestBuilder.makeHttpRequest(request);
                        resourceName = r.getUrl();
                        dataOutputStream.writeBytes(request);
                        dataOutputStream.flush();
                    }
                } catch (IOException ex) {

                }
            }
        }).start();
    }

    /**
     *
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     *
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     *
     * @param socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getListObject() {
        return listObject;
    }

    /**
     *
     * @param listObject
     */
    public void setListObject(ArrayList<String> listObject) {
        this.listObject = listObject;
    }

}
