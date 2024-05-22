package FullRelease;

public class HealthPotion {
    private int count;

    public HealthPotion() {
        count = 0;
    }

    public void addPotion() {
        count++;
    }

    public boolean usePotion() {
        if (count > 0) {
            count--;
            return true;
        }
        return false;
    }

    public int getCount() {
        return count;
    }
}
