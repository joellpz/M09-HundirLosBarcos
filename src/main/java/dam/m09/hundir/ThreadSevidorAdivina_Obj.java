package dam.m09.hundir;

import java.io.*;
import java.net.Socket;

public class ThreadSevidorAdivina_Obj implements Runnable {
    /* Thread que gestiona la comunicació de SrvTcPAdivina_Obj.java i un cllient ClientTcpAdivina_Obj.java */
    private Socket clientSocket;
    private InputStream in;
    private OutputStream out;
    private Board board;
    private String jugada;

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
        try {
            while(true) {
                //Enviem board al jugador
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(board);
                oos.flush();

                //Llegim la resposta del Client
                ObjectInputStream ois = new ObjectInputStream(in);
                try {
                    jugada = (String) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                board.input(jugada);
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