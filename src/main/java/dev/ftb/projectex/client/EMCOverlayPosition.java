package dev.ftb.projectex.client;

public enum EMCOverlayPosition {
    DISABLED("Disabled"),
    TOP_LEFT("Top-Left"),
    TOP_RIGHT("Top-Right");

    private final String string;

    EMCOverlayPosition(String s) {
        string = s;
    }

    @Override
    public String toString() {
        return string;
    }
}
