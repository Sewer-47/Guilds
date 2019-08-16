package me.sewer.guilds.teleport;

import java.util.Queue;

public class TeleportTask implements Runnable {

    private final Queue<Teleport> teleportList;

    public TeleportTask(Queue<Teleport> teleportList) {
        this.teleportList = teleportList;
    }

    @Override
    public void run() {
        for (Teleport teleporter : this.teleportList) {
            if (teleporter.tryTeleport()) {
                this.teleportList.remove(teleporter);
            }
        }
    }
}