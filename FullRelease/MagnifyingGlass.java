package FullRelease;

public class MagnifyingGlass {
    private boolean hasMagnifyingGlass = false;
    private boolean nextRoundLive = false;  // Stores the live status of the next round

    public boolean hasMagnifyingGlass() {
        return hasMagnifyingGlass;
    }

    public void acquireMagnifyingGlass() {
        hasMagnifyingGlass = true;
    }

    public void setNextRoundLive(boolean isLive) {
        nextRoundLive = isLive;
    }

    public String checkRound() {
        // Debug output to trace what the magnifying glass knows
        System.out.println("DEBUG: Magnifying Glass checked - Next round is " + (nextRoundLive ? "LIVE" : "BLANK"));
        return nextRoundLive ? "Next round is LIVE!" : "Next round is BLANK!";
    }

    // Method to help debug the status set by the game
    public boolean getNextRoundLive() {
        return nextRoundLive;
    }
}
