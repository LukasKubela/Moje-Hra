package FullRelease;

public class MagnifyingGlass {
    private boolean hasMagnifyingGlass;
    private boolean nextRoundLive;

    public void acquireMagnifyingGlass() {
        this.hasMagnifyingGlass = true;
    }

    public boolean hasMagnifyingGlass() {
        return hasMagnifyingGlass;
    }

    public void setNextRoundLive(boolean nextRoundLive) {
        this.nextRoundLive = nextRoundLive;
    }

    public String checkRound() {
        return "The next round is " + (nextRoundLive ? "BLANK" : "LIVE");  // Always returns the opposite of the actual round state
    }
}
