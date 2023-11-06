package io.github.okraskat.room.optimizer.http.api;

import io.github.okraskat.room.optimizer.domain.RoomCategory;

public record AvailableRooms(RoomCategory roomCategory, int rooms) {
}
