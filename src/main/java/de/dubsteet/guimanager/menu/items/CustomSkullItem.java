package de.dubsteet.guimanager.menu.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.UUID;


public abstract class CustomSkullItem extends CustomItem {

    public CustomSkullItem() {
        setCustomItemMeta();
    }

    protected void setCustomItemMeta() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = (PlayerProfile) Bukkit.createPlayerProfile(UUID.fromString(getCustomUUID()), getCustomName());

        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(getTextureUrl()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        meta.setPlayerProfile(profile);
        setItemMeta(meta);
    }

    @Override
    public @NotNull Material getCustomItemMaterial() {
        return Material.PLAYER_HEAD;
    }

    @NotNull
    protected abstract String getCustomUUID();

    @NotNull
    protected abstract String getCustomName();

    @NotNull
    protected abstract String getTextureUrl();
}
