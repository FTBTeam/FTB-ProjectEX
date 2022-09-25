package dev.ftb.projectex.config;

public enum ScreenPosition {
    DISABLED("Disabled"),
    TOP_LEFT("Top-Left"),
    TOP_RIGHT("Top-Right");

    private final String string;

    ScreenPosition(String s) {
        string = s;
    }

    @Override
    public String toString() {
        return string;
    }
}
