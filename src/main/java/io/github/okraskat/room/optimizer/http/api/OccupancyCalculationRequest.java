package io.github.okraskat.room.optimizer.http.api;

import io.github.okraskat.room.optimizer.domain.RoomCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record OccupancyCalculationRequest(@NotNull @Size(min = 1) Map<RoomCategory, Integer> availableRooms,
                                          @NotNull @Size(min = 1) List<BigDecimal> potentialPayments) {
}
