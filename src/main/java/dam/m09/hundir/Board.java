package dam.m09.hundir;

import java.util.ArrayList;
import java.util.List;

public class Board {
    int [][] board = new int[8][8];
    /* El tablero se define por 4 posibles valores en cada posición.
     0. Indica que esa casilla esta vacia.
     1. Indica posicion ocupada por barco.
     2. Indica posicion vacia ya marcada.
     3. Indica barco tocado.
    */

    List<Boat> barcos = new ArrayList<>();

    Boat b1 = new Boat(5)

}
