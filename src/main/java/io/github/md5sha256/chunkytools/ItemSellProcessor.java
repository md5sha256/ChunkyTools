package io.github.md5sha256.chunkytools;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.OptionalDouble;
import java.util.function.Predicate;

public class ItemSellProcessor implements ItemProcessor {

    private final PaymentProcessor paymentProcessor;
    private final Shop shop;
    private final Predicate<ItemStack> itemFilter;

    public ItemSellProcessor(PaymentProcessor paymentProcessor, Shop shop, Predicate<ItemStack> itemFilter) {
        this.paymentProcessor = paymentProcessor;
        this.shop = shop;
        this.itemFilter = itemFilter;
    }

    @Override
    public void processItems(CommandSender sender, Iterable<Item> items) {
        if (!(sender instanceof Player player)) {
            return;
        }
        double balanceToDeposit = 0;
        for (Item item : items) {
            final ItemStack itemStack = item.getItemStack();
            if (!this.itemFilter.test(itemStack)) {
                return;
            }
            final OptionalDouble price = this.shop.price(itemStack);
            if (price.isPresent()) {
                balanceToDeposit += price.getAsDouble();
                item.remove();
            }
        }
        this.paymentProcessor.addBalance(player.getUniqueId(), balanceToDeposit);
    }
}
