package io.github.okraskat.room.optimizer.domain;

import java.math.BigDecimal;
import java.util.*;

class OccupancyCalculation {

    private final Map<RoomCategory, Integer> initialAvailableRooms;
    private final Map<RoomCategory, Integer> availableRooms;
    private final Map<RoomCategory, LinkedList<BigDecimal>> paymentsPerCategory;

    private final List<RoomCategory> categoriesSortedByPriceDesc;
    private final Set<RoomCategory> registeredUpgradedCategories;

    OccupancyCalculation(Map<RoomCategory, Integer> availableRooms, List<RoomCategory> categoriesSortedByPriceDesc) {
        this.availableRooms = new HashMap<>(availableRooms);
        this.initialAvailableRooms = new HashMap<>(availableRooms);
        this.paymentsPerCategory = new HashMap<>();
        this.categoriesSortedByPriceDesc = categoriesSortedByPriceDesc;
        this.registeredUpgradedCategories = new HashSet<>();
    }

    void tryToOccupyRoom(RoomCategory roomCategory, BigDecimal payment) {
        Integer availableRooms = this.availableRooms.getOrDefault(roomCategory, 0);
        if (availableRooms == 0 && !this.registeredUpgradedCategories.contains(roomCategory)) {
            tryToOccupyWithHighestPaymentUpgrade(roomCategory, payment);
        } else if (availableRooms > 0) {
            bookOccupancy(roomCategory, payment);
        }
    }

    List<CalculatedOccupancy> getResults() {
        return paymentsPerCategory.entrySet()
                .stream()
                .map(e -> new CalculatedOccupancy(e.getKey(), getOccupiedRooms(e), getPotentialProfit(e)))
                .toList();
    }

    private void tryToOccupyWithHighestPaymentUpgrade(RoomCategory roomCategory, BigDecimal payment) {
        for (int i = 0; i < this.categoriesSortedByPriceDesc.size(); i++) {
            if (this.categoriesSortedByPriceDesc.get(i) == roomCategory && i > 0) {
                RoomCategory upgradeCategory = this.categoriesSortedByPriceDesc.get(i - 1);
                if (this.availableRooms.getOrDefault(upgradeCategory, 0) > 0) {
                    upgradePayment(roomCategory, payment, upgradeCategory);
                }
            }
        }
    }

    private void upgradePayment(RoomCategory roomCategory, BigDecimal payment, RoomCategory upgradeCategory) {
        Optional.of(this.paymentsPerCategory.get(roomCategory))
                .map(LinkedList::peekFirst)
                .ifPresent(t -> {
                    freeRoom(roomCategory, t);
                    bookOccupancy(upgradeCategory, t);
                    bookOccupancy(roomCategory, payment);
                    this.registeredUpgradedCategories.add(roomCategory);
                });
    }

    private void bookOccupancy(RoomCategory roomCategory, BigDecimal payment) {
        this.availableRooms.put(roomCategory, this.availableRooms.get(roomCategory) - 1);
        LinkedList<BigDecimal> payments = paymentsPerCategory.getOrDefault(roomCategory, new LinkedList<>());
        payments.add(payment);
        this.paymentsPerCategory.put(roomCategory, payments);
    }

    private static BigDecimal getPotentialProfit(Map.Entry<RoomCategory, LinkedList<BigDecimal>> entry) {
        return entry.getValue().stream()
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private int getOccupiedRooms(Map.Entry<RoomCategory, LinkedList<BigDecimal>> entry) {
        return initialAvailableRooms.get(entry.getKey()) - availableRooms.get(entry.getKey());
    }

    private void freeRoom(RoomCategory roomCategory, BigDecimal payment) {
        availableRooms.put(roomCategory, availableRooms.get(roomCategory) + 1);
        LinkedList<BigDecimal> payments = paymentsPerCategory.get(roomCategory);
        payments.removeFirstOccurrence(payment);
        paymentsPerCategory.put(roomCategory, payments);
    }
}
