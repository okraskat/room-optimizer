package io.github.okraskat.room.optimizer.http.api;

import java.util.List;

public record OccupancyCalculationResponse(List<DesiredOccupancy> desiredOccupancies) {
}
