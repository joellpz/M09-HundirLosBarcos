package dam.m09.hundir;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

public class ClientTcpAdivina_Obj extends Thread {
    /* CLient TCP que ha endevinar un número pensat per SrvTcpAdivina_Obj.java */
    private String Nom;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Scanner scin;
    private boolean continueConnected;
    private Board board;
    private boolean firstTry = true;
    Pattern pattern = Pattern.compile("[a-h][0-7]");
    Matcher matcher;

    private ClientTcpAdivina_Obj(String hostname, int port) {
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        continueConnected = true;
        scin = new Scanner(System.in);
    }

    public void run() {
        String msg = null;
        boolean rep;
        while (continueConnected) {
            //Llegir info del servidor (estat del board)
            board = getRequest();

            //Mostrar el tablero al cliente
            board.showBoardNoBoats();

            if (firstTry) {
                System.out.println("Comença la partida!");
                firstTry = false;
            } else {
                //Llegir condicions
                if (board.isTouched()) {
                    if (board.isDestroyed()) System.out.println("BOOOOOM - Vaixell destruït!!");
                    else System.out.println("Has tocat un vaixell!");
                } else {
                    System.out.println("No s'ha tocat cap vaixell...");
                }

                if (board.isFin()) {
                    System.out.println("S'ha acabat la partida! Tots els vaixells s'han enfonsat!");
                    continueConnected = false;
                }
            }


            //Pedir una posicion
            do {
                System.out.print("Quina posició del taulell vols atacar? Ex. (a1): ");

                //Comproba si s'escull una posició correcte
                msg = scin.next();
                matcher = pattern.matcher(msg);

                //Comproba si s'escull una posició ja marcada
                if (!matcher.matches() || board.posRepetida(msg)) {
                    rep = true;
                    if (!matcher.matches())
                        System.out.println(" ** Escriu la posició amb el format indicat a l'exemple ** ");
                    else System.out.println(" ** Escriu una posició no seleccionada anteriorment ** ");
                } else rep = false;
            } while (rep);

            try {
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(msg);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        close(socket);
    }

    private Board getRequest() {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            board = (Board) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return board;
    }


    private void close(Socket socket) {
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if (socket != null && !socket.isClosed()) {
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }
                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(ClientTcpAdivina_Obj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        String jugador, ipSrv;

        //Demanem la ip del servidor i nom del jugador
        System.out.println("Ip del servidor?");
        Scanner sip = new Scanner(System.in);
        ipSrv = sip.next();
        System.out.println("Nom jugador:");
        jugador = sip.next();

        ClientTcpAdivina_Obj clientTcp = new ClientTcpAdivina_Obj(ipSrv, 5558);
        clientTcp.Nom = jugador;
        clientTcp.start();
    }
}