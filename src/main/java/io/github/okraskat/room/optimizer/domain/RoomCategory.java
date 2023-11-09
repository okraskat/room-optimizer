package io.github.okraskat.room.optimizer.domain;

import java.math.BigDecimal;
import java.util.Optional;

public enum RoomCategory {
    PREMIUM(BigDecimal.valueOf(100), null),
    ECONOMY(BigDecimal.ZERO, BigDecimal.valueOf(100))
    ;

    private final BigDecimal lowestAvailablePrice;
    private final BigDecimal highestAvailablePrice;

    RoomCategory(BigDecimal lowestAvailablePrice, BigDecimal highestAvailablePrice) {
        this.lowestAvailablePrice = lowestAvailablePrice;
        this.highestAvailablePrice = highestAvailablePrice;
    }

    boolean isPaymentInCategoryRange(BigDecimal payment) {
        boolean highestPriceMatches = Optional.ofNullable(highestAvailablePrice)
                .map(h -> h.compareTo(payment) >= 0)
                .orElse(true);
        return payment.compareTo(lowestAvailablePrice) >= 0 && highestPriceMatches;
    }

    BigDecimal getLowestAvailablePrice() {
        return lowestAvailablePrice;
    }
}
