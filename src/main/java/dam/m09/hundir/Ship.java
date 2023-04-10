package dam.m09.hundir;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ship implements Serializable {
    int length;
    List<String> positions;
    List<Boolean> touched;
    boolean destroyed;

    public Ship(int length, List<String> positions) {
        this.length = length;
        this.positions = positions;
        touched = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            touched.add(false);
        }
        destroyed = false;
    }

    public boolean isTouched(String pos) {
        if (positions.contains(pos.toLowerCase())) {
            touched.set(positions.indexOf(pos.toLowerCase()), true);
            destroyed = isDestroyed();
            return true;
        } else return false;
    }

    public boolean isDestroyed() {
        return !touched.contains(false);
    }

}
