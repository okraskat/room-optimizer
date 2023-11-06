package io.github.okraskat.room.optimizer.http.api;

import io.github.okraskat.room.optimizer.domain.RoomCategory;

record DesiredOccupancy(RoomCategory roomCategory, int usedRooms, int potentialProfit) {
}
