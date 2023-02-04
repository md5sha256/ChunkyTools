package io.github.md5sha256.chunkytools;

import com.github.md5sha256.spigotutils.blocks.BlockPosition;
import com.github.md5sha256.spigotutils.blocks.ChunkPosition;
import io.papermc.lib.PaperLib;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HopperRegistrar implements Listener {

    private Map<ChunkPosition, Set<BlockPosition>> hoppers = new HashMap<>();

    public HopperRegistrar(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void registerHopper(Block block) {
        final ChunkPosition chunkPos = new ChunkPosition(block.getChunk());
        final BlockPosition blockPos = new BlockPosition(block);
        hoppers.computeIfAbsent(chunkPos, x -> new HashSet<>()).add(blockPos);
    }

    private void removeHopper(Block block) {
        ChunkPosition position = new ChunkPosition(block.getChunk());
        final Set<BlockPosition> knownHoppers = this.hoppers.get(position);
        if (knownHoppers == null) {
            return;
        }
        if (knownHoppers.remove(new BlockPosition(block)) && knownHoppers.isEmpty()) {
            this.hoppers.remove(position);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getType() != Material.HOPPER) {
            return;
        }
       registerHopper(event.getBlock());
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().forEach(this::removeHopper);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        removeHopper(event.getBlock());
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent event) {
        ChunkPosition position = new ChunkPosition(event.getLocation().getChunk());
        Set<BlockPosition> knownHoppers = hoppers.getOrDefault(position, Collections.emptySet());
        if (knownHoppers.isEmpty()) {
            return;
        }
        final ItemStack dropped = event.getEntity().getItemStack();
        Map<Integer, ItemStack> result = new HashMap<>();
        result.put(0, dropped);
        for (BlockPosition blockPosition : knownHoppers) {
            if (result.isEmpty()) {
                return;
            }
            Block block = blockPosition.getBlock();
            BlockState state = PaperLib.getBlockState(block, false).getState();
            if (!(state instanceof BlockInventoryHolder inventoryHolder)) {
                continue;
            }
            Inventory inventory = inventoryHolder.getInventory();
            result = inventory.addItem(result.get(0));
        }
        if (result.isEmpty()) {
            event.setCancelled(true);
        } else {
            event.getEntity().setItemStack(result.get(0));
        }
    }

}
