package io.github.okraskat.room.optimizer.domain;

import java.util.Optional;

public enum RoomCategory {
    PREMIUM(100, null),
    ECONOMY(0, 100)
    ;

    private final int lowestAvailablePrice;
    private final Integer highestAvailablePrice;

    RoomCategory(int lowestAvailablePrice, Integer highestAvailablePrice) {
        this.lowestAvailablePrice = lowestAvailablePrice;
        this.highestAvailablePrice = highestAvailablePrice;
    }

    boolean isPaymentInCategoryRange(int payment) {
        Boolean highestPriceMatches = Optional.ofNullable(highestAvailablePrice)
                .map(h -> h >= payment)
                .orElse(true);
        return payment >= lowestAvailablePrice && highestPriceMatches;
    }

    int getLowestAvailablePrice() {
        return lowestAvailablePrice;
    }
}
