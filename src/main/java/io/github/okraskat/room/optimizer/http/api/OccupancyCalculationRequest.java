package io.github.okraskat.room.optimizer.http.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OccupancyCalculationRequest(@NotNull @Size(min = 1) @Valid List<AvailableRooms> availableRooms,
                                          @NotNull @Size(min = 1) List<Integer> potentialPayments) {
}
