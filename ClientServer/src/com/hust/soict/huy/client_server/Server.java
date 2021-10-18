package com.hust.soict.huy.client_server;

import com.hust.soict.huy.helper.SelectionSort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("The Sorter Server is running");
        int clientNumber = 0;
        try(ServerSocket listener = new ServerSocket(9898)){
            while(true){
                new Sorter(listener.accept(),clientNumber++).start();
            }
        }
    }

    private static class Sorter extends Thread {
        private Socket socket;
        private int clientNumber;

        public Sorter(Socket socket, int clientNumber){
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New client #"+clientNumber+" connected at "+socket);
        }

        public void run(){
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                out.println("Hello, you are client #"+clientNumber);

                while(0==0){
                    String input = in.readLine();
                    if(input == null || input.isEmpty()){
                        break;
                    }
                    String[] nums = input.split(" ");
                    int[] intarr = new int[nums.length];
                    int i=0;
                    for( String textValue:nums){
                        intarr[i] = Integer.parseInt(textValue);
                        i++;
                    }

                    new SelectionSort().sort(intarr);

                    String strArray[] = Arrays.stream(intarr).mapToObj(String::valueOf).toArray(String[]::new);
                    out.println(Arrays.toString(strArray));
                }
            } catch (IOException e) {
                System.out.println("Error handling client #"+clientNumber);
            } finally {
                try{
                    socket.close();
                }catch (IOException e){
                    System.out.println("Connection with client #"+clientNumber+" closed");
                }
            }
        }

    }

}
