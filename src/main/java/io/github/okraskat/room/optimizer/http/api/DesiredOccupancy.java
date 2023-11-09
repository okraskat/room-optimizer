package io.github.okraskat.room.optimizer.http.api;

import io.github.okraskat.room.optimizer.domain.RoomCategory;

import java.math.BigDecimal;

record DesiredOccupancy(RoomCategory roomCategory, int usedRooms, BigDecimal potentialProfit) {
}
