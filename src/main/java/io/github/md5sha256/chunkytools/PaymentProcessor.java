package io.github.md5sha256.chunkytools;

import java.util.UUID;

public interface PaymentProcessor {

    boolean addBalance(UUID player, double balance);

    boolean removeBalance(UUID player, double balance);

    double balance(UUID player);

}
