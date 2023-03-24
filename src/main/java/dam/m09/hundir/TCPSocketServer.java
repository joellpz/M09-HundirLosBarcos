package dam.m09.hundir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPSocketServer {

    public static void main(String[] args) {
        TCPSocketServer server = new TCPSocketServer();
        server.listen();
    }

    private final Scanner sc = new Scanner(System.in);
    static final int PORT = 9090;

    public void listen() {
        ServerSocket serverSocket;
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(PORT);

            boolean end = false;
            while (!end) {
                clientSocket = serverSocket.accept();
                System.out.println("CLIENT CONNECTAT");
                //processem la petició del client
                proccesClientRequest(clientSocket);
//tanquem el sòcol temporal per atendre el client
                closeClient(clientSocket);
            }
//tanquem el sòcol principal
            if ((serverSocket != null) && !serverSocket.isClosed()) {
                serverSocket.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(TCPSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void proccesClientRequest(Socket clientSocket) {
        boolean farewellMessage;
        String clientMessage = "";
        BufferedReader in;
        PrintStream out;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintStream(clientSocket.getOutputStream());
            System.out.println("Canals FETS");
            do {
//processem el missatge del client i generem la resposta. Si
//clientMessage és buida generarem el missatge de benvinguda
                String dataToSend = processData(clientMessage);
                out.println(dataToSend);
                out.flush();
                clientMessage = in.readLine();
                farewellMessage = isFarewellMessage(clientMessage);
            } while ((clientMessage) != null && !farewellMessage);
        } catch (IOException ex) {
            Logger.getLogger(TCPSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isFarewellMessage(String clientMessage) {
        return clientMessage.equals("bye");
    }

    private String processData(String clientMessage) {
        System.out.println(clientMessage);
        return sc.nextLine();
    }

    private void closeClient(Socket clientSocket) {
//si falla el tancament no podem fer gaire cosa, només enregistrar
//el problema
        try {
//tancament de tots els recursos
            if (clientSocket != null && !clientSocket.isClosed()) {
                if (!clientSocket.isInputShutdown()) {
                    clientSocket.shutdownInput();
                }
                if (!clientSocket.isOutputShutdown()) {
                    clientSocket.shutdownOutput();
                }
                clientSocket.close();
            }
        } catch (IOException ex) {
//enregistrem l'error amb un objecte Logger
            Logger.getLogger(TCPSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
