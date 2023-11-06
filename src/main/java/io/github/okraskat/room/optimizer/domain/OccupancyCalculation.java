package io.github.okraskat.room.optimizer.domain;

import java.util.*;

class OccupancyCalculation {

    private final Map<RoomCategory, Integer> availableRooms;
    private final Map<RoomCategory, LinkedList<Integer>> paymentsPerCategory;

    private final List<RoomCategory> categoriesSortedByPriceDesc;
    private final Set<RoomCategory> registeredUpgradedCategories;

    OccupancyCalculation(Map<RoomCategory, Integer> availableRooms, List<RoomCategory> categoriesSortedByPriceDesc) {
        this.availableRooms = new HashMap<>(availableRooms);
        this.paymentsPerCategory = new HashMap<>();
        this.categoriesSortedByPriceDesc = categoriesSortedByPriceDesc;
        this.registeredUpgradedCategories = new HashSet<>();
    }

    void tryToOccupyRoom(RoomCategory roomCategory, int payment) {
        int availableRooms = this.availableRooms.get(roomCategory);
        if (availableRooms == 0 && !this.registeredUpgradedCategories.contains(roomCategory)) {
            tryToOccupyWithHighestPaymentUpgrade(roomCategory, payment);
        } else if (availableRooms > 0) {
            bookOccupancy(roomCategory, payment);
        }
    }

    List<CalculatedOccupancy> getResults() {
        return paymentsPerCategory.entrySet()
                .stream()
                .map(e -> new CalculatedOccupancy(e.getKey(), this.paymentsPerCategory.get(e.getKey()).size(), getPotentialProfit(e)))
                .toList();
    }

    private void tryToOccupyWithHighestPaymentUpgrade(RoomCategory roomCategory, int payment) {
        for (int i = 0; i < this.categoriesSortedByPriceDesc.size(); i++) {
            if (this.categoriesSortedByPriceDesc.get(i) == roomCategory && i > 0) {
                RoomCategory upgradeCategory = this.categoriesSortedByPriceDesc.get(i - 1);
                if (this.availableRooms.get(upgradeCategory) > 0) {
                    upgradePayment(roomCategory, payment, upgradeCategory);
                }
            }
        }
    }

    private void upgradePayment(RoomCategory roomCategory, int payment, RoomCategory upgradeCategory) {
        Optional.of(this.paymentsPerCategory.get(roomCategory))
                .map(LinkedList::peekFirst)
                .ifPresent(t -> {
                    freeRoom(roomCategory, t);
                    bookOccupancy(upgradeCategory, t);
                    bookOccupancy(roomCategory, payment);
                    this.registeredUpgradedCategories.add(roomCategory);
                });
    }

    private void bookOccupancy(RoomCategory roomCategory, int payment) {
        this.availableRooms.put(roomCategory, this.availableRooms.get(roomCategory) - 1);
        LinkedList<Integer> payments = paymentsPerCategory.getOrDefault(roomCategory, new LinkedList<>());
        payments.add(payment);
        this.paymentsPerCategory.put(roomCategory, payments);
    }

    private static Integer getPotentialProfit(Map.Entry<RoomCategory, LinkedList<Integer>> entru) {
        return entru.getValue().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private void freeRoom(RoomCategory roomCategory, int payment) {
        availableRooms.put(roomCategory, availableRooms.get(roomCategory) + 1);
        LinkedList<Integer> payments = paymentsPerCategory.get(roomCategory);
        payments.removeFirstOccurrence(payment);
        paymentsPerCategory.put(roomCategory, payments);
    }
}
