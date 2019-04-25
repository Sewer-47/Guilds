package me.sewer.guilds.tablist;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.sewer.guilds.user.User;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Tablist {

    private final Map<Integer, String> slots;
    private final String header;
    private final String footer;
    private final ProtocolManager protocolManager;
    private final Random random;

    public Tablist(Map<Integer, String> slots, String header, String footer) {
        this.slots = new ConcurrentHashMap<>(slots); //Async
        this.header = header;
        this.footer = footer;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.random = new Random();
    }

    public void update(User user) {

        UUID uniqueId = new UUID(this.random.nextLong(), this.random.nextLong());
        Player player = user.getBukkit().get();
        if (player != null) {

            PacketContainer playerListHeaderFooter = new PacketContainer(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
            PacketContainer playerInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);

            playerListHeaderFooter.getChatComponents().write(0, WrappedChatComponent.fromText(this.header));
            playerListHeaderFooter.getChatComponents().write(1, WrappedChatComponent.fromText(this.footer));


            playerInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
            List<PlayerInfoData> infoData = new ArrayList<>();

            this.slots.entrySet().forEach(entry -> {
                PlayerInfoData playerInfoData = new PlayerInfoData(new WrappedGameProfile(uniqueId, ""),
                        0, EnumWrappers.NativeGameMode.NOT_SET,
                        WrappedChatComponent.fromText(entry.getValue()));
                if (!infoData.contains(playerInfoData)) {
                    infoData.add(playerInfoData);
                }
            });
            playerInfo.getPlayerInfoDataLists().write(0, infoData);

            try {
                this.protocolManager.sendServerPacket(player, playerInfo);
                this.protocolManager.sendServerPacket(player, playerListHeaderFooter);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}