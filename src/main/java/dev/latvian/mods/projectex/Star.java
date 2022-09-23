package dev.latvian.mods.projectex;

import org.jetbrains.annotations.Nullable;

public enum Star {
    EIN("ein", "Ein"),
    ZWEI("zwei", "Zwei"),
    DREI("drei", "Drei"),
    VIER("vier", "Vier"),
    SPHERE("sphere", "Sphere"),
    OMEGA("omega", "Omega");

    public static final Star[] VALUES = values();

    public final String name;
    public final String displayName;

    Star(String n, String d) {
        name = n;
        displayName = d;
    }

    @Nullable
    public Star getPrev() {
        return this == EIN ? null : VALUES[ordinal() - 1];
    }
}
