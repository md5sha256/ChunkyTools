package io.github.md5sha256.chunkytools;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.OptionalDouble;
import java.util.UUID;
import java.util.function.Predicate;

public class InventorySellProcessor implements InventoryProcessor {
    private final PaymentProcessor paymentProcessor;
    private final Shop shop;
    private final Predicate<ItemStack> itemFilter;

    public InventorySellProcessor(PaymentProcessor paymentProcessor,
                                  Shop shop,
                                  Predicate<ItemStack> itemFilter
    ) {
        this.paymentProcessor = paymentProcessor;
        this.shop = shop;
        this.itemFilter = itemFilter;
    }

    @Override
    public void processInventories(CommandSender sender, Iterable<Inventory> inventories) {
        if (!(sender instanceof Player player)) {
            return;
        }
        final UUID playerUuid = player.getUniqueId();
        double balanceToDeposit = 0;
        int count = 0;
        for (Inventory inventory : inventories) {
            final ItemStack[] contents = inventory.getContents();
            boolean changed = false;
            for (int i = 0; i < contents.length; i++) {
                ItemStack item = contents[i];
                if (item == null || !this.itemFilter.test(item)) {
                    continue;
                }
                OptionalDouble price = this.shop.price(item);
                if (price.isPresent()) {
                    contents[i] = null;
                    changed = true;
                    count += item.getAmount();
                    balanceToDeposit += price.getAsDouble();
                }
            }
            if (changed) {
                inventory.setContents(contents);
            }
        }
        this.paymentProcessor.addBalance(playerUuid, balanceToDeposit);
        sender.sendMessage(String.format("You have sold %d number of cacti for a total of %f dollars", count, balanceToDeposit));
    }

}
