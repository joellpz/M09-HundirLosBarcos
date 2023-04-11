package dam.m09.hundir;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SrvTcpAdivina_Obj {
    /* Servidor TCP que genera un número perquè ClientTcpAdivina_Obj.java jugui a encertar-lo
     * i on la comunicació dels diferents jugadors la gestionaran els Threads : ThreadServidorAdivina_Obj.java
     * */
    private int port;
    private int rnd;
    private Board board;

    private SrvTcpAdivina_Obj(int port, int rnd) {
        this.port = port;
        this.rnd = rnd;
    }

    public SrvTcpAdivina_Obj(int port, Board board) {
        this.port = port;
        this.board = board;
    }

    private void listen() {
        ServerSocket serverSocket;
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                clientSocket = serverSocket.accept();
                ThreadSevidorAdivina_Obj FilServidor;
                if (board == null) FilServidor = new ThreadSevidorAdivina_Obj(clientSocket, new Board(rnd));
                else FilServidor = new ThreadSevidorAdivina_Obj(clientSocket, board);

                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(SrvTcpAdivina_Obj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String opt;
        SrvTcpAdivina_Obj srv;
        do {
            System.out.println("Vols definir els vaixells (D) o un taulell aleatori (R)");
            opt = sc.nextLine();
            if (!opt.equalsIgnoreCase("D") && !opt.equalsIgnoreCase("R")) {
                System.out.println(" ** ARGUMENT ERRONI, Torna a intentar! ** ");
            }
        } while (!opt.equalsIgnoreCase("D") && !opt.equalsIgnoreCase("R"));

        if (opt.equalsIgnoreCase("R")) {
            srv = new SrvTcpAdivina_Obj(5558, new Random().nextInt(3));
        } else srv = new SrvTcpAdivina_Obj(5558, new Board());

        srv.listen();
    }
}
