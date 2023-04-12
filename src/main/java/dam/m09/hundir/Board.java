package dam.m09.hundir;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Board implements Serializable {
    private String[][] board = new String[8][8];

    /* El tablero se define por 4 posibles valores en cada posición.
     null. Indica que esa casilla esta vacia.
     O. Indica posicion vacia ya marcada.
     X. Indica Ship tocado.
    */
    private List<Ship> ships;
    private boolean fin, touched, destroyed;

    public Board(int num) {
        fin = false;
        touched = false;
        destroyed = false;
        switch (num) {
            case 0 -> ships = new ArrayList<>(List.of(
                    new Ship(5, new ArrayList<>(Arrays.asList("a1", "b1", "c1", "d1", "e1"))),
                    new Ship(4, new ArrayList<>(Arrays.asList("b3", "b4", "b5", "b6"))),
                    new Ship(3, new ArrayList<>(Arrays.asList("d3", "e3", "f3"))),
                    new Ship(3, new ArrayList<>(Arrays.asList("h3", "h4", "h5"))),
                    new Ship(2, new ArrayList<>(Arrays.asList("f5", "f6"))),
                    new Ship(2, new ArrayList<>(Arrays.asList("d6", "d7"))),
                    new Ship(2, new ArrayList<>(Arrays.asList("a6", "a7"))),
                    new Ship(1, new ArrayList<>(Arrays.asList("g7")))
            ));
            case 1 -> ships = new ArrayList<>(List.of(
                    new Ship(5, new ArrayList<>(Arrays.asList("c5", "d5", "e5", "f5", "g5"))),
                    new Ship(4, new ArrayList<>(Arrays.asList("c4", "d4", "e4", "f4"))),
                    new Ship(3, new ArrayList<>(Arrays.asList("c3", "d3", "e3"))),
                    new Ship(3, new ArrayList<>(Arrays.asList("b5", "b6", "b7"))),
                    new Ship(2, new ArrayList<>(Arrays.asList("a1", "b1"))),
                    new Ship(2, new ArrayList<>(Arrays.asList("g1", "h1"))),
                    new Ship(2, new ArrayList<>(Arrays.asList("e7", "f7"))),
                    new Ship(1, new ArrayList<>(Arrays.asList("f6")))
            ));
            case 2 -> ships = new ArrayList<>(List.of(
                    new Ship(5, new ArrayList<>(Arrays.asList("a1", "a2", "a3", "a4", "a5"))),
                    new Ship(4, new ArrayList<>(Arrays.asList("b3", "b4", "b5", "b6"))),
                    new Ship(3, new ArrayList<>(Arrays.asList("c1", "c2", "c3"))),
                    new Ship(3, new ArrayList<>(Arrays.asList("f4", "g4", "h4"))),
                    new Ship(2, new ArrayList<>(Arrays.asList("d6", "d7"))),
                    new Ship(2, new ArrayList<>(Arrays.asList("e3", "e4"))),
                    new Ship(2, new ArrayList<>(Arrays.asList("f6", "f7"))),
                    new Ship(1, new ArrayList<>(Arrays.asList("h1")))
            ));
        }
    }

    public Board() {
        ships = new ArrayList<>();
        fin = false;
        touched = false;
        destroyed = false;
        buildBoard();
    }

    public Board(Board board) {
        fin = false;
        touched = false;
        destroyed = false;
        ships = new ArrayList<>();

        for (Ship ship : board.ships) {
            ships.add(new Ship(ship));
        }

    }

    public void buildBoard() {
        Scanner sc = new Scanner(System.in);
        String orient, letter, pos;
        int number = 0;
        boolean rep;
        int[] lenghts = new int[]{1, 3, 2, 1, 1};
        for (int i = 0; i < lenghts.length; i++) {
            for (int j = 0; j < lenghts[i]; j++) {
                showBoardWithBoats();
                do {
                    rep = false;
                    System.out.println(" ** Defineix la orientació del vaixell (H/V) ** ");
                    orient = sc.nextLine();
                    if (!orient.equalsIgnoreCase("H") && !orient.equalsIgnoreCase("V")) {

                        rep = true;
                    }
                } while (rep);
                do {
                    rep = false;

                    System.out.println(" ** Escriu la columna on col·locar el vaixeill (length:" + (i + 1) + ") **");
                    letter = sc.nextLine();
                    if (!letter.equalsIgnoreCase("a") && !letter.equalsIgnoreCase("b") && !letter.equalsIgnoreCase("c") &&
                            !letter.equalsIgnoreCase("d") && !letter.equalsIgnoreCase("e") && !letter.equalsIgnoreCase("f") &&
                            !letter.equalsIgnoreCase("g") && !letter.equalsIgnoreCase("h")) {
                        System.out.println(" ** ARGUMENT ERRONI, Torna a intentar! ** ");
                        rep = true;
                    } else {
                        System.out.println(" ** Escriu la fila on col·locar el vaixell **");
                        try {
                            number = Integer.parseInt(sc.nextLine());

                            if (number < 0 || number > 7) {
                                throw new NumberFormatException();
                            }
                            if (!ubicarShip(new Ship(i + 1), letter.concat(number + ""), orient)) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println(" ** ARGUMENT ERRONI, Torna a intentar! ** ");
                            rep = true;
                        }

                    }
                } while (rep);
            }
        }
    }

    public boolean ubicarShip(Ship ship, String pos, String orientation) {
        // Verificamos que el Ship cabe en el tablero
        int column, row;
        column = traduceLetter(pos);
        row = getRow(pos);


        if (orientation.equalsIgnoreCase("H")) {
            if ((column + ship.getLength()) > board.length) {
                return false;
            }
        } else if (orientation.equalsIgnoreCase("V")) {
            if ((row + ship.getLength()) > board.length) {
                return false;
            }
        }

        // Verificamos que no haya otro Ship en la ubicación deseada
        for (int i = 0; i < ship.getLength(); i++) {
            for (Ship boat : ships) {
                if (orientation.equalsIgnoreCase("H")) {
                    if (boat.getPositions().contains(traduceNumber(column + i).concat(row + ""))) return false;
                } else if (orientation.equalsIgnoreCase("V")) {
                    if (boat.getPositions().contains(traduceNumber(column).concat((row + i) + ""))) return false;
                }
            }
        }

        // Si tod o está bien, ubicamos el Ship
        List<String> positions = new ArrayList<>();
        for (int i = 0; i < ship.getLength(); i++) {
            if (orientation.equalsIgnoreCase("H")) {
                positions.add(traduceNumber(column + i).concat(row + ""));
            } else if (orientation.equalsIgnoreCase("V")) {
                positions.add(traduceNumber(column).concat((row + i) + ""));
            }
        }
        ship.setPositions(positions);
        ships.add(ship);
        return true;

    }


    public void showBoardNoBoats() {
        System.out.println("    |-A-|-B-|-C-|-D-|-E-|-F-|-G-|-H-|");
        for (int i = 0; i < board.length; i++) {
            System.out.print("| " + i + " ");
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null) System.out.print("|   ");
                else System.out.print("| " + board[i][j] + " ");
            }
            System.out.println("|");
            System.out.println("|---|---|---|---|---|---|---|---|---|");
        }
    }

    public void showBoardWithBoats() {
        System.out.println("    |-A-|-B-|-C-|-D-|-E-|-F-|-G-|-H-|");
        boolean active = false;
        for (int i = 0; i < board.length; i++) {
            System.out.print("| " + i + " ");
            for (int j = 0; j < board.length; j++) {
                if (ships.size() != 0) {
                    active = false;
                    for (Ship ship : ships) {
                        if (ship.getPositions().contains(traduceNumber(j) + i)) {
                            System.out.print("| X ");
                            active = true;
                            break;
                        }
                    }
                }
                if (board[i][j] == null && !active) System.out.print("|   ");
            }
            System.out.println("|");
            System.out.println("|---|---|---|---|---|---|---|---|---|");
        }
    }

    public void input(String pos) {
        destroyed = false;
        if (!(touched = checkIfBoat(pos))) {
            board[getRow(pos)][traduceLetter(pos)] = "O";
            fin = isFinished();
        } else board[getRow(pos)][traduceLetter(pos)] = "X";
    }

    public boolean checkIfBoat(String pos) {
        for (Ship ship : ships) {
            if (ship.isTouched(pos)) {
                if (ship.isDestroyed()) destroyed = true;
                return true;
            }
        }
        return false;
    }

    public boolean isFinished() {
        for (Ship ship : ships) {
            if (!ship.isDestroyed()) return false;
        }
        return true;
    }

    private int traduceLetter(String pos) {
        return switch (pos.split("")[0].toLowerCase()) {
            case "a" -> 0;
            case "b" -> 1;
            case "c" -> 2;
            case "d" -> 3;
            case "e" -> 4;
            case "f" -> 5;
            case "g" -> 6;
            case "h" -> 7;
            default -> -1;
        };
    }

    private String traduceNumber(int num) {
        return switch (num) {
            case 0 -> "a";
            case 1 -> "b";
            case 2 -> "c";
            case 3 -> "d";
            case 4 -> "e";
            case 5 -> "f";
            case 6 -> "g";
            case 7 -> "h";
            default -> "z";
        };
    }

    private int getRow(String pos) {
        return Integer.parseInt(pos.split("")[1]);
    }

    public boolean posRepetida(String pos) {
        if (board[getRow(pos)][traduceLetter(pos)] == null) return false;
        else return true;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public String[][] getBoard() {
        return board;
    }

    public boolean isFin() {
        return fin;
    }

    public boolean isTouched() {
        return touched;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
