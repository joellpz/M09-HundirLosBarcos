package dam.m09.hundir;

import java.util.ArrayList;
import java.util.List;

public class Boat {
    int length;
    List<String> positions;
    List<Boolean> touched;
    boolean destroyed;

    public Boat(int length, List<String> positions) {
        this.length = length;
        this.positions = positions;
        touched = new ArrayList<>();
        destroyed = false;
    }

    public boolean isTouched(String pos) {
        if (positions.contains(pos)) {
            touched.add(positions.indexOf(pos), true);
            destroyed = isDestroyed();
            return true;
        } else return false;
    }

    public boolean isDestroyed() {
        return !touched.contains(false);
    }

}
