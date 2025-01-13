package net.fathommod;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.Music;

public class ClientDevUtils {
    public static void playMusic(Music music) {
        if (!Minecraft.getInstance().getMusicManager().isPlayingMusic(music)) {
            Minecraft.getInstance().getMusicManager().startPlaying(music);
        }
    }
}
