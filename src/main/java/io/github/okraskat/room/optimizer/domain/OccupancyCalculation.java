package io.github.okraskat.room.optimizer.domain;

import java.util.*;

class OccupancyCalculation {

    private final Map<RoomCategory, Integer> initialAvailableRooms;
    private final Map<RoomCategory, Integer> availableRooms;
    private final Map<RoomCategory, Integer> profitsPerCategory;

    OccupancyCalculation(Map<RoomCategory, Integer> availableRooms) {
        this.availableRooms = new HashMap<>(availableRooms);
        this.initialAvailableRooms = new HashMap<>(availableRooms);
        this.profitsPerCategory = new HashMap<>();
    }

    void tryToOccupyRoom(RoomCategory roomCategory, int payment) {
        int availableRooms = this.availableRooms.get(roomCategory);
        if (availableRooms > 0) {
            bookOccupancy(roomCategory, payment);
        }
    }

    List<CalculatedOccupancy> getResults() {
        return profitsPerCategory.entrySet()
                .stream()
                .map(e -> new CalculatedOccupancy(e.getKey(), getOccupiedRooms(e.getKey()), profitsPerCategory.get(e.getKey())))
                .toList();
    }

    private void bookOccupancy(RoomCategory roomCategory, int payment) {
        this.availableRooms.put(roomCategory, this.availableRooms.get(roomCategory) - 1);
        Integer profit = profitsPerCategory.getOrDefault(roomCategory, 0);
        this.profitsPerCategory.put(roomCategory, profit + payment);
    }

    private int getOccupiedRooms(RoomCategory roomCategory) {
        return initialAvailableRooms.get(roomCategory) - availableRooms.get(roomCategory);
    }

}
