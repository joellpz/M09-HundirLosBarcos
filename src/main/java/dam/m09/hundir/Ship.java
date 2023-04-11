package dam.m09.hundir;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ship implements Serializable {
    private int length;
    private List<String> positions;
    private List<Boolean> touched;
    private boolean destroyed;

    public Ship(int length, List<String> positions) {
        this.length = length;
        setPositions(positions);
        destroyed = false;
    }

    public Ship(int length) {
        this.length = length;
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

    public int getLength() {
        return length;
    }

    public List<String> getPositions() {
        return positions;
    }

    public List<Boolean> getTouched() {
        return touched;
    }

    public void setPositions(List<String> positions) {
        this.positions = positions;
        touched = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            touched.add(false);
        }
    }
}
