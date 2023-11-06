package io.github.okraskat.room.optimizer.http.api;

import io.github.okraskat.room.optimizer.domain.RoomCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AvailableRooms(@NotNull RoomCategory roomCategory, @Min(1) int rooms) {
}
