package io.github.md5sha256.chunkytools;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChunkyCommandManager {

    private final PaperCommandManager<CommandSender> commandManager;
    private final SimplePluginState pluginState;

    public ChunkyCommandManager(Plugin plugin, SimplePluginState pluginState) throws Exception {
        this.pluginState = pluginState;
        commandManager = PaperCommandManager.createNative(plugin,
                CommandExecutionCoordinator.simpleCoordinator());
        commandManager.registerBrigadier();
        commandManager.registerAsynchronousCompletions();
        initSellChestCommand();
    }

    public void initSellChestCommand() {
        var command = commandManager.commandBuilder("sellchest",
                        CommandMeta.simple().build(),
                        "sc")
                .permission("sellchest.sc")
                .senderType(Player.class)
                .handler(ctx -> {
                    Player player = (Player) ctx.getSender();
                    Chunk chunk = player.getLocation().getChunk();
                    ChunkInventoryProcessor inventoryProcessor = new ChunkInventoryProcessor(
                            pluginState.inventoryProcessor());
                    inventoryProcessor.processChunk(player, chunk);
                })
                .build();
        commandManager.command(command);
    }

}
