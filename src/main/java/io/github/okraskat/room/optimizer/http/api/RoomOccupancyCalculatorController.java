package io.github.okraskat.room.optimizer.http.api;

import io.github.okraskat.room.optimizer.domain.CalculatedOccupancy;
import io.github.okraskat.room.optimizer.domain.OccupancyCalculatorApi;
import io.github.okraskat.room.optimizer.domain.RoomCategory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/occupancy-calculations")
class RoomOccupancyCalculatorController {

    private final OccupancyCalculatorApi occupancyCalculatorApi;

    RoomOccupancyCalculatorController(OccupancyCalculatorApi occupancyCalculatorApi) {
        this.occupancyCalculatorApi = occupancyCalculatorApi;
    }

    @PostMapping
    OccupancyCalculationResponse calculate(@Validated @RequestBody OccupancyCalculationRequest calculationRequest) {
        Map<RoomCategory, Integer> availableRoomsPerCategory = calculationRequest.availableRooms()
                .stream()
                .collect(Collectors.toMap(AvailableRooms::roomCategory, AvailableRooms::rooms, Integer::sum));
        List<CalculatedOccupancy> calculatedOccupancies = occupancyCalculatorApi.calculate(availableRoomsPerCategory,
                calculationRequest.potentialPayments());
        return new OccupancyCalculationResponse(calculatedOccupancies.stream()
                .map(c -> new DesiredOccupancy(c.roomCategory(), c.occupiedRooms(), c.potentialIncome()))
                .toList());
    }

}
