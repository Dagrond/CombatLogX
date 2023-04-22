package combatlogx.expansion.force.field.configuration;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.github.sirblobman.api.configuration.IConfigurable;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class ForceFieldConfiguration implements IConfigurable {
    private boolean enabled;
    private XMaterial material;
    private int radius;
    private String bypassPermissionName;

    private transient Permission bypassPermission;

    public ForceFieldConfiguration() {
        this.enabled = true;
        this.material = XMaterial.RED_STAINED_GLASS;
        this.radius = 8;
        this.bypassPermissionName = "combatlogx.bypass.force.field";
    }

    @Override
    public void load(@NotNull ConfigurationSection section) {
        boolean enabled = section.getBoolean("enabled", true);
        setEnabled(enabled);

        String materialName = section.getString("material");
        if (materialName == null) {
            materialName = "RED_STAINED_GLASS";
        }

        Optional<XMaterial> optionalMaterial = XMaterial.matchXMaterial(materialName);
        setMaterial(optionalMaterial.orElse(XMaterial.RED_STAINED_GLASS));

        int radius = section.getInt("radius", 8);
        setRadius(radius);

        String bypassPermissionName = section.getString("bypass-permission",
                "combatlogx.bypass.force.field");
        setBypassPermissionName(bypassPermissionName);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public @NotNull XMaterial getMaterial() {
        return this.material;
    }

    public void setMaterial(@NotNull XMaterial material) {
        this.material = material;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius < 1) {
            throw new IllegalArgumentException("radius must be at least one.");
        }

        this.radius = radius;
    }

    public @Nullable String getBypassPermissionName() {
        return bypassPermissionName;
    }

    public void setBypassPermissionName(@Nullable String bypassPermissionName) {
        this.bypassPermissionName = bypassPermissionName;
        this.bypassPermission = null;
    }

    public @Nullable Permission getBypassPermission() {
        if (this.bypassPermission != null) {
            return this.bypassPermission;
        }

        String permissionName = getBypassPermissionName();
        if (permissionName == null || permissionName.isEmpty()) {
            return null;
        }

        String description = "CombatLogX Force Field Bypass";
        this.bypassPermission = new Permission(permissionName, description, PermissionDefault.FALSE);
        return this.bypassPermission;
    }
}
