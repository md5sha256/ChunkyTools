package io.github.md5sha256.chunkytools;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

public class SimpleShop implements Shop {

    private Map<ItemStack, Double> priceMap = new HashMap<>();

    @Override
    public OptionalDouble unitPrice(ItemStack itemStack) {
        ItemStack item;
        if (itemStack.getAmount() != 1) {
            item = itemStack.clone();
            item.setAmount(1);
        } else {
            item = itemStack;
        }
        final Double price = this.priceMap.get(item);
        if (price == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(price);
    }

    @Override
    public void unitPrice(ItemStack itemStack, double newPrice) {
        ItemStack item;
        if (itemStack.getAmount() != 1) {
            item = itemStack.clone();
            item.setAmount(1);
        } else {
            item = itemStack;
        }
        this.priceMap.put(item, newPrice / itemStack.getAmount());
    }
}
