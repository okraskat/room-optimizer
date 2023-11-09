package io.github.okraskat.room.optimizer.domain;

import java.math.BigDecimal;

public record CalculatedOccupancy(RoomCategory roomCategory, int occupiedRooms, BigDecimal potentialIncome) {
}
