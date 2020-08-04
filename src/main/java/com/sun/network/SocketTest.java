package com.sun.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class SocketTest {

    public static void main(String[] args) {

        new Server().doMain(args);

        //new Client().doMain(args);

    }


}


class Client {

    public void doMain(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 6379);
            OutputStream outputStream = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String word = reader.readLine();
                if(word != null) {
                    byte[] array = word.getBytes();
                    for(byte b: array) {
                        outputStream.write(b);
                    }
                    //outputStream.write(word.getBytes());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class Server {

    public void doMain(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress socketAddress = new InetSocketAddress("localhost", 6379);
            serverSocket.bind(socketAddress);

            while (true) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();

                byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) > 0) {
                    for(int i=0;i<read;i++) {
                        System.out.println(buffer[i]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
