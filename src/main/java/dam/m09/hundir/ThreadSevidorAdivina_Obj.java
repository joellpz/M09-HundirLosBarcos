package dam.m09.hundir;

import java.io.*;
import java.net.Socket;

public class ThreadSevidorAdivina_Obj implements Runnable {
    /* Thread que gestiona la comunicació de SrvTcPAdivina_Obj.java i un cllient ClientTcpAdivina_Obj.java */

    private Socket clientSocket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private Board board;
    private boolean acabat;

    public ThreadSevidorAdivina_Obj(Socket clientSocket, Board board) throws IOException {
        this.clientSocket = clientSocket;
        this.board = board;

        //Enllacem els canals de comunicació
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
        System.out.println("canals i/o creats amb un nou jugador");
    }

    @Override
    public void run() {
        Jugada j = null;
        try {
            while(!acabat) {

                //Enviem board al jugador
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(board);
                oos.flush();

                //Llegim la jugada
                ObjectInputStream ois = new ObjectInputStream(in);
                try {
                    j = (Jugada) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("jugada: " + j.Nom + "->" + j.num);
                if(!board.map_jugadors.containsKey(j.Nom)) board.map_jugadors.put(j.Nom, 1);
                else {
                    //Si el judador ja esxiteix, actualitzem la quatitat de tirades
                    int tirades = board.map_jugadors.get(j.Nom) + 1;
                    board.map_jugadors.put(j.Nom, tirades);
                }

                //comprobar la jugada i actualitzar board amb el resultat de la jugada
                board.resultat = ns.comprova(j.num);
                if(board.resultat == 0) {
                    acabat = true;
                    System.out.println(j.Nom + " l'ha encertat");
                    board.acabats++;
                }
            }
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }
        //Enviem últim estat del board abans de acabar amb la comunicació i acabem
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(board);
            oos.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}