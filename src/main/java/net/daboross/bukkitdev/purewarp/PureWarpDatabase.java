/*
 * Copyright (C) 2013 daboross
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.bukkitdev.purewarp;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 *
 * @author daboross
 */
public class PureWarpDatabase {

    private final PureWarpPlugin plugin;
    private File saveFile;
    private YamlConfiguration save;
    private final Map<String, PureWarp> warps = new HashMap<String, PureWarp>();

    public PureWarpDatabase(PureWarpPlugin plugin) {
        this.plugin = plugin;
        ConfigurationSerialization.registerClass(PureWarp.class);
    }

    public void load() {
        if (save != null) {
            return;
        }
        if (saveFile == null) {
            saveFile = new File(plugin.getDataFolder(), "warps.yml");
        }
        save = new YamlConfiguration();
        save.options().indent(2).header("PureWarp warp database.\nPlease do not edit by hand.");
        if (saveFile.exists()) {
            try {
                save.load(saveFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Exception reading database", ex);
            } catch (InvalidConfigurationException ex) {
                plugin.getLogger().log(Level.SEVERE, "Exception reading database", ex);
            }
        }
        Set<String> keys = save.getKeys(false);
        for (String key : keys) {
            Object obj = save.get(key);
            if (!(obj instanceof PureWarp)) {
                plugin.getLogger().warning("Warp not instanceof PureWarp");
                continue;
            }
            warps.put(key.toLowerCase(), (PureWarp) save.get(key));
        }
    }

    public void save() {
        if (save == null) {
            load();
        }
        for (Map.Entry<String, PureWarp> entry : warps.entrySet()) {
            save.set(entry.getKey(), entry.getValue());
        }
        try {
            save.save(saveFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Exception saving database", ex);
        }
    }

    public void setWarp(String name, PureWarp warp) {
        warps.put(name.toLowerCase(), warp);
    }

    public PureWarp getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public boolean removeWarp(String name) {
        return warps.remove(name) != null;
    }

    public Set<String> getWarpList() {
        return Collections.unmodifiableSet(warps.keySet());
    }
}
