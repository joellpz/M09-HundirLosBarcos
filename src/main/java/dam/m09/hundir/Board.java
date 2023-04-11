package dam.m09.hundir;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Board implements Serializable {
    String[][] board = new String[8][8];
    /* El tablero se define por 4 posibles valores en cada posición.
     null. Indica que esa casilla esta vacia.
     O. Indica posicion vacia ya marcada.
     X. Indica Ship tocado.
    */
    List<Ship> ships;
    boolean fin, touched;

    public Board(int num) {
        fin = false;
        touched = false;
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
        fin = false;
        touched = false;
        buildBoard();
    }

    public void buildBoard() {
        Scanner sc = new Scanner(System.in);
        String orient, letter, pos;
        int number;
        boolean rep;
        int[] lenghts = new int[]{1, 3, 2, 1, 5};
        for (int i = 0; i < lenghts.length; i++) {
            for (int j = 0; j < lenghts[i]; j++) {
                do {
                    rep = false;
                    System.out.println(" ** Define the orientation of the ship (H/V) ** ");
                    orient = sc.nextLine();
                    if (!orient.equalsIgnoreCase("H") && !orient.equalsIgnoreCase("V")) {

                        rep = true;
                    }
                } while (rep);
                do {
                    rep = false;
                    showBoardNoBoats();
                    System.out.println(" ** Write the Column to put the Ship **");
                    letter = sc.nextLine();
                    if (!letter.equalsIgnoreCase("a") && !letter.equalsIgnoreCase("b") && !letter.equalsIgnoreCase("c") &&
                            !letter.equalsIgnoreCase("d") && !letter.equalsIgnoreCase("e") && !letter.equalsIgnoreCase("f") &&
                            !letter.equalsIgnoreCase("g") && !letter.equalsIgnoreCase("h")) {
                        System.out.println(" ** ERROR. BAD ARGUMENT, Try again! **");
                        rep = true;
                    } else {
                        System.out.println(" ** Write the Row to put the Ship **");
                        try {
                            number = sc.nextInt();
                            if (number < 1 && number > 8){
                                throw new NumberFormatException();
                            }
                            ubicarShip(new Ship(i), letter.concat(number+""),orient);
                        } catch (Exception e) {
                            System.out.println(" ** ERROR. BAD ARGUMENT, Try again! **");
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

        if (ship.getPositions().size() != 0) {

            if (orientation == "H") {
                if (column + ship.getLength() > board.length) {
                    return false;
                }
            } else if (orientation == "V") {
                if (row + ship.getLength() > board.length) {
                    return false;
                }
            }

            // Verificamos que no haya otro Ship en la ubicación deseada
            for (int i = 0; i < ship.getLength(); i++) {
                if (orientation == "H") {
                    if (board[row][column + i] != null) {
                        return false;
                    }
                } else if (orientation == "V") {
                    if (board[row + i][column] != null) {
                        return false;
                    }
                }
            }

            // Si tod o está bien, ubicamos el Ship
            List<String> positions = new ArrayList<>();
            for (int i = 0; i < ship.getLength(); i++) {
                if (orientation == "H") {
                    positions.add(traduceNumber(column + i).concat(row + ""));
                } else if (orientation == "V") {
                    positions.add(traduceNumber(column).concat((row + i) + ""));
                }
            }
            ship.setPositions(positions);
            ships.add(ship);
            return true;
        }
        return false;
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

    public void input(String pos) {
        if (!(touched = checkIfBoat(pos))) {
            board[getRow(pos)][traduceLetter(pos)] = "O";
            fin = isFinished();
        } else board[getRow(pos)][traduceLetter(pos)] = "X";
    }

    public boolean checkIfBoat(String pos) {
        for (Ship ship : ships) {
            if (ship.isTouched(pos)) return true;
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
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> "z";
        };
    }

    private int getRow(String pos) {
        return Integer.parseInt(pos.split("")[1]);
    }

    public boolean posRepetida(String pos) {
        if (board[Integer.parseInt(pos.split("")[1])][traduceLetter(pos.split("")[0])] == "") return false;
        else return true;
    }
}
