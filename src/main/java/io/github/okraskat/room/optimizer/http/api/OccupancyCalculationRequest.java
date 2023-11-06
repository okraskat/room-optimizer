package io.github.okraskat.room.optimizer.http.api;

import java.util.List;

public record OccupancyCalculationRequest(List<AvailableRooms> availableRooms,
                                          List<Integer> potentialPayments) {
}
