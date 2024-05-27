package FullRelease;

public class Freeze {
    private boolean isFrozen;

    public void activateFreeze() {
        isFrozen = true;
    }

    public void deactivateFreeze() {
        isFrozen = false;
    }

    public boolean isFrozen() {
        return isFrozen;
    }
}
