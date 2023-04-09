package dam.m09.hundir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Board {
    String[][] board = new String[8][8];
    /* El tablero se define por 4 posibles valores en cada posición.
     null. Indica que esa casilla esta vacia.
     O. Indica posicion vacia ya marcada.
     X. Indica barco tocado.
    */
    List<Boat> barcos;

    public Board() {
        switch (new Random().nextInt(3)) {
            case 0 -> barcos = new ArrayList<>(List.of(
                    new Boat(5, new ArrayList<>(Arrays.asList("a1", "b1", "c1", "d1", "e1"))),
                    new Boat(4, new ArrayList<>(Arrays.asList("b3", "b4", "b5", "b6"))),
                    new Boat(3, new ArrayList<>(Arrays.asList("d3", "e3", "f3"))),
                    new Boat(3, new ArrayList<>(Arrays.asList("h3", "h4", "h5"))),
                    new Boat(2, new ArrayList<>(Arrays.asList("f5", "f6"))),
                    new Boat(2, new ArrayList<>(Arrays.asList("d6", "d7"))),
                    new Boat(2, new ArrayList<>(Arrays.asList("a7", "a8"))),
                    new Boat(1, new ArrayList<>(Arrays.asList("g8")))
            ));
            case 1 -> barcos = new ArrayList<>(List.of(
                    new Boat(5, new ArrayList<>(Arrays.asList("c5", "d5", "e5", "f5", "g5"))),
                    new Boat(4, new ArrayList<>(Arrays.asList("c4", "d4", "e4", "f4"))),
                    new Boat(3, new ArrayList<>(Arrays.asList("c3", "d3", "e3"))),
                    new Boat(3, new ArrayList<>(Arrays.asList("b5", "b6", "b7"))),
                    new Boat(2, new ArrayList<>(Arrays.asList("a1", "b1"))),
                    new Boat(2, new ArrayList<>(Arrays.asList("g1", "h1"))),
                    new Boat(2, new ArrayList<>(Arrays.asList("e7", "f7"))),
                    new Boat(1, new ArrayList<>(Arrays.asList("f6")))
            ));
            case 2 -> barcos = new ArrayList<>(List.of(
                    new Boat(5, new ArrayList<>(Arrays.asList("a1", "a2", "a3", "a4", "a5"))),
                    new Boat(4, new ArrayList<>(Arrays.asList("b3", "b4", "b5", "b6"))),
                    new Boat(3, new ArrayList<>(Arrays.asList("c1", "c2", "c3"))),
                    new Boat(3, new ArrayList<>(Arrays.asList("f4", "g4", "h4"))),
                    new Boat(2, new ArrayList<>(Arrays.asList("d6", "d7"))),
                    new Boat(2, new ArrayList<>(Arrays.asList("e3", "e4"))),
                    new Boat(2, new ArrayList<>(Arrays.asList("f6", "f7"))),
                    new Boat(1, new ArrayList<>(Arrays.asList("h1")))
            ));
        }
    }

    public void showBoardNoBoats() {
        System.out.println("|-A-|-B-|-C-|-D-|-E-|-F-|-G-|-H-|");
        for (int i = 0; i < board.length; i++) {

            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null) System.out.print("|   ");
                else System.out.print("| " + board[i][j] + " ");
            }
            System.out.println("|");
            System.out.println("|---|---|---|---|---|---|---|---|");
        }
    }

    public void input(String pos) {
        if (!checkIfBoat(pos)) board[traducePos(pos.split("")[0])][Integer.parseInt(pos.split("")[0])] = "O";
        else board[traducePos(pos.split("")[0])][Integer.parseInt(pos.split("")[0])] = "X";
    }

    public boolean checkIfBoat(String pos) {
        for (Boat boat : barcos) {
            if (boat.isTouched(pos)) return true;
        }
        return false;
    }

    private int traducePos(String posLetter) {
        return switch (posLetter) {
            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            case "E" -> 4;
            case "F" -> 5;
            case "G" -> 6;
            case "H" -> 7;
            default -> -1;
        };
    }

}
