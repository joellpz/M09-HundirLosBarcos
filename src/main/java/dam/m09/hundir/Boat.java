package dam.m09.hundir;

import java.util.List;

public class Boat {
    int length;
    List<String> positions;
    List<Boolean> touched;
    public Boat(int length, List<String> positions) {
        this.length = length;
        this.positions = positions;
    }

}
