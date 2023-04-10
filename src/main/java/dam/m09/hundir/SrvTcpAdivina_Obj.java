package dam.m09.hundir;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SrvTcpAdivina_Obj {
    /* Servidor TCP que genera un número perquè ClientTcpAdivina_Obj.java jugui a encertar-lo
     * i on la comunicació dels diferents jugadors la gestionaran els Threads : ThreadServidorAdivina_Obj.java
     * */

    private int port;
    private Board board;

    private SrvTcpAdivina_Obj(int port ) {
        this.port = port;
        board = new Board();
    }

    private void listen() {
        ServerSocket serverSocket;
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                clientSocket = serverSocket.accept();
                ThreadSevidorAdivina_Obj FilServidor = new ThreadSevidorAdivina_Obj(clientSocket, board);
                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(SrvTcpAdivina_Obj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        SrvTcpAdivina_Obj srv = new SrvTcpAdivina_Obj(5558);
        srv.listen();
    }
}
