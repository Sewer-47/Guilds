package me.sewer.guilds.teleport;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TeleportManager {

    private final Queue<Teleport> teleportList;

    public TeleportManager() {
        this.teleportList = new ConcurrentLinkedQueue<>();
    }

    public void addTeleport(Teleport teleport) {
        this.teleportList.add(teleport);
    }

    public Queue<Teleport> getTeleportList() {
        return this.teleportList;
    }
}
