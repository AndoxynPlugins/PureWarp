/*
 * Copyright (C) 2013-2014 Dabo Ross <http://www.daboross.net/>
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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;

/**
 *
 * @author daboross
 */
@SerializableAs("SkyLocationAccurate")
public class PureWarp implements ConfigurationSerializable {

    public final double x;
    public final double y;
    public final double z;
    public final double yaw;
    public final double pitch;
    public final String world;

    public PureWarp(double x, double y, double z, double yaw, double pitch, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public PureWarp(double x, double y, double z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
        this.world = world;
    }

    public PureWarp(Block block) {
        this(block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
    }

    public PureWarp(Location location) {
        this(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld().getName());
    }

    public PureWarp(Entity entity) {
        this(entity.getLocation());
    }

    public PureWarp add(double x, double y, double z) {
        return new PureWarp(this.x + x, this.y + y, this.z + z, world);
    }

    public PureWarp add(PureWarp location) {
        return new PureWarp(this.x + location.x, this.y + location.y, this.z + location.z, world);
    }

    public Location toLocation() {
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null) {
            Bukkit.getLogger().log(Level.WARNING, "[PureWarp] World ''{0}'' not found!", world);
            return null;
        }
        return new Location(bukkitWorld, x, y, z);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("xpos", x);
        map.put("ypos", y);
        map.put("zpos", z);
        map.put("yaw", yaw);
        map.put("pitch", pitch);
        map.put("world", world);
        return map;
    }

    public static PureWarp deserialize(Map<String, Object> map) {
        Object worldO = map.get("world");
        Double x = get(map.get("xpos")),
                y = get(map.get("ypos")),
                z = get(map.get("zpos")),
                yaw = get(map.get("yaw")),
                pitch = get(map.get("pitch"));
        if (x == null || y == null || z == null || worldO == null) {
            return null;
        }
        String world = worldO.toString();
        if (yaw != null && pitch != null) {
            return new PureWarp(x, y, z, yaw, pitch, world);
        } else {
            return new PureWarp(x, y, z, world);
        }
    }

    private static Double get(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Integer) {
            return Double.valueOf(((Integer) o).doubleValue());
        }
        if (o instanceof Double) {
            return (Double) o;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PureWarp)) {
            return false;
        }
        PureWarp l = (PureWarp) obj;
        return l.x == x && l.y == y && l.z == z && l.world.equals(world);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        hash = 97 * hash + (this.world != null ? this.world.hashCode() : 0);
        return hash;
    }
}
