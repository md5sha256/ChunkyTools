package io.github.md5sha256.chunkytools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class ChunkyToolsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Optional<VaultPaymentProcessor> optionalPaymentProcessor
                = VaultPaymentProcessor.tryCreateVaultPaymentProcessor();
        if (optionalPaymentProcessor.isEmpty()) {
            getLogger().severe("Could not initialize the vault payment processor!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        PaymentProcessor paymentProcessor = optionalPaymentProcessor.get();
        Shop shop = new SimpleShop();
        shop.price(new ItemStack(Material.CACTUS, 1), 10);
        InventoryProcessor inventoryProcessor = new InventorySellProcessor(paymentProcessor,
                shop,
                x -> true);
        ItemProcessor itemProcessor = new ItemSellProcessor(paymentProcessor,
                shop,
                x -> true);
        SimplePluginState pluginState = new SimplePluginState(paymentProcessor, shop, inventoryProcessor, itemProcessor);
        try {
            new ChunkyCommandManager(this, pluginState);
        } catch (Exception ex) {
            getLogger().throwing("SellChestPlugin", "onEnable", ex);
        }
        new HopperRegistrar(this);
        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }
}
