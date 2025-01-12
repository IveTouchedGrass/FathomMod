package net.fathommod;

import net.minecraft.client.KeyMapping;

import java.util.ArrayList;

public class ClientVars {
    public static double movementHeldTimeTicks = 0;
    public static long clientTickAge = 0;
    public static boolean canAutoAttack = false;
    public static final ArrayList<KeyMapping> pressedKeys = new ArrayList<>();
    public static ArrayList<KeyMapping> lastPressedKeys = new ArrayList<>();
}
