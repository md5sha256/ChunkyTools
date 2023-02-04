package io.github.md5sha256.chunkytools;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Optional;
import java.util.UUID;

public class VaultPaymentProcessor implements PaymentProcessor {

    private final Economy economy;

    public VaultPaymentProcessor(Economy economy) {
        this.economy = economy;
    }

    public static Optional<VaultPaymentProcessor> tryCreateVaultPaymentProcessor() {
        RegisteredServiceProvider<Economy> ecoProvider = Bukkit.getServicesManager()
                .getRegistration(Economy.class);
        return Optional.ofNullable(ecoProvider)
                .map(RegisteredServiceProvider::getProvider)
                .map(VaultPaymentProcessor::new);
    }

    private void ensureAccount(OfflinePlayer player) {
        if (!economy.hasAccount(player)) {
            economy.createPlayerAccount(player);
        }
    }

    @Override
    public boolean addBalance(UUID player, double balance) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        ensureAccount(offlinePlayer);
        return economy.depositPlayer(offlinePlayer, balance).transactionSuccess();
    }

    @Override
    public boolean removeBalance(UUID player, double balance) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        ensureAccount(offlinePlayer);
        return economy.withdrawPlayer(offlinePlayer, balance).transactionSuccess();
    }

    @Override
    public double balance(UUID player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        if (!economy.hasAccount(offlinePlayer)) {
            return 0;
        }
        return economy.getBalance(offlinePlayer);
    }
}
