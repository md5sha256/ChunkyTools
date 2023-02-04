package io.github.md5sha256.chunkytools;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Collection;

public class ChunkItemEntityProcessor {

    private final ItemProcessor processor;

    public ChunkItemEntityProcessor(ItemProcessor processor) {
        this.processor = processor;
    }

    public void processChunk(CommandSender sender, Chunk chunk) {
        Entity[] entities = chunk.getEntities();
        Collection<Item> items = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Item item) {
                items.add(item);
            }
        }
        this.processor.processItems(sender, items);

    }

}
