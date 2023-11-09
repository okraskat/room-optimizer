package io.github.okraskat.room.optimizer.domain;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
class OccupancyCalculator implements OccupancyCalculatorApi {

    @Override
    public List<CalculatedOccupancy> calculate(Map<RoomCategory, Integer> availableRoomsPerCategory, Collection<BigDecimal> potentialPayments) {
        TreeSet<BigDecimal> sortedPayments = new TreeSet<>(potentialPayments);
        List<RoomCategory> categoriesSortedByPriceDesc = Arrays.asList(RoomCategory.values());
        categoriesSortedByPriceDesc.sort(Comparator.comparing(RoomCategory::getLowestAvailablePrice).reversed());
        OccupancyCalculation occupancyCalculation = new OccupancyCalculation(availableRoomsPerCategory, categoriesSortedByPriceDesc);

        while (!sortedPayments.isEmpty()) {
            BigDecimal highestPayment = Objects.requireNonNull(sortedPayments.pollLast());
            categoriesSortedByPriceDesc.stream()
                    .filter(c -> c.isPaymentInCategoryRange(highestPayment))
                    .findFirst()
                    .ifPresent(c -> occupancyCalculation.tryToOccupyRoom(c, highestPayment));

        }
        return occupancyCalculation.getResults();
    }

}
