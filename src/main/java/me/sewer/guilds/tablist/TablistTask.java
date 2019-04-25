package me.sewer.guilds.tablist;

import me.sewer.guilds.user.UserManager;

public class TablistTask implements Runnable {

    private final Tablist tabList;
    private final UserManager userManager;

    public TablistTask(Tablist tabList, UserManager userManager) {
        this.tabList = tabList;
        this.userManager = userManager;
    }

    @Override
    public void run() {
        this.userManager.getOnline().forEach(user -> {
            this.tabList.update(user);
        });
    }
}
