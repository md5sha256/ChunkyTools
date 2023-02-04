package io.github.md5sha256.chunkytools;

public record SimplePluginState(
        PaymentProcessor paymentProcessor,
        Shop shop,
        InventoryProcessor inventoryProcessor,
        ItemProcessor itemProcessor
) {

}
