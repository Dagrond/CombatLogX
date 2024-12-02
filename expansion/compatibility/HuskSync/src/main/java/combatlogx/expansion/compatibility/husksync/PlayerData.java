package combatlogx.expansion.compatibility.husksync;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PlayerData {
    private final Player player;
    private final Location location;

    private ItemStack[] oldInventory;
    private List<ItemStack> itemsToKeep;
    private boolean keepInventory;
    private boolean keepLevel;
    private int totalExperience;
    private int newLevel;
    private float newExperience;

    public PlayerData(@NotNull Player player, @NotNull Location location) {
        this.player = player;
        this.location = location;
        this.oldInventory = new ItemStack[0];
        this.itemsToKeep = new ArrayList<>();

        this.keepInventory = false;
        this.keepLevel = false;
        this.totalExperience = 0;
        this.newLevel = 0;
        this.newExperience = 0.0F;
    }

    public @NotNull Player getPlayer() {
        return this.player;
    }

    public @NotNull Location getLocation() {
        return this.location;
    }

    public boolean isKeepInventory() {
        return this.keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public boolean isKeepLevel() {
        return this.keepLevel;
    }

    public void setKeepLevel(boolean keepLevel) {
        this.keepLevel = keepLevel;
    }

    public int getTotalExperience() {
        return this.totalExperience;
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }

    public int getNewLevel() {
        return this.newLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    public float getNewExperience() {
        return this.newExperience;
    }

    public void setNewExperience(float newExperience) {
        this.newExperience = newExperience;
    }

    public void setOldInventory(ItemStack @NotNull [] clone) {
        this.oldInventory = clone;
    }
    public void setItemsToKeep(List<ItemStack> clone) {
        this.itemsToKeep.addAll(clone);
    }

    public ItemStack @NotNull [] getOldInventory() {
        return this.oldInventory;
    }
    public List<ItemStack> getItemsToKeep() {
        return this.itemsToKeep;
    }
}
