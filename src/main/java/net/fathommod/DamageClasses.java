package net.fathommod;

public enum DamageClasses {
    MELEE(0x00ff00),
    ASSASSIN(0x9d00ff),
    RANGED(0xff0000),
    SUMMON(0x00ffff);

    private final int color;

    DamageClasses(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public String getComponent() {
        return switch (this) {
            case MELEE
                    -> "fathommod.damage_classes.melee";
            case ASSASSIN
                    -> "fathommod.damage_classes.assassin";
            case RANGED
                    -> "fathommod.damage_classes.ranged";
            case SUMMON
                    -> "fathommod.damage_classes.summon";
        };
    }
}
