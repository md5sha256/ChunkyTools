package io.github.md5sha256.chunkytools;

import org.bukkit.inventory.ItemStack;

import java.util.OptionalDouble;

public interface Shop {

   default OptionalDouble price(ItemStack itemStack) {
       OptionalDouble optional = unitPrice(itemStack);
       if (optional.isPresent()) {
           return OptionalDouble.of(optional.getAsDouble() * itemStack.getAmount());
       }
       return optional;
   }

    OptionalDouble unitPrice(ItemStack itemStack);

    default void price(ItemStack itemStack, double newPrice) {
        double newUnitPrice = newPrice / itemStack.getAmount();
        unitPrice(itemStack, newUnitPrice);
    }

    void unitPrice(ItemStack itemStack, double newPrice);

}
