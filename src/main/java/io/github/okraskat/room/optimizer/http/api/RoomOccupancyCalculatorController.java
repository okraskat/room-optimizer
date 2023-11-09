package io.github.okraskat.room.optimizer.http.api;

import io.github.okraskat.room.optimizer.domain.CalculatedOccupancy;
import io.github.okraskat.room.optimizer.domain.OccupancyCalculatorApi;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/occupancy-calculations")
class RoomOccupancyCalculatorController {

    private final OccupancyCalculatorApi occupancyCalculatorApi;

    RoomOccupancyCalculatorController(OccupancyCalculatorApi occupancyCalculatorApi) {
        this.occupancyCalculatorApi = occupancyCalculatorApi;
    }

    @PostMapping
    OccupancyCalculationResponse calculate(@Validated @RequestBody OccupancyCalculationRequest calculationRequest) {
        List<CalculatedOccupancy> calculatedOccupancies = occupancyCalculatorApi.calculate(calculationRequest.availableRooms(),
                calculationRequest.potentialPayments());
        return new OccupancyCalculationResponse(calculatedOccupancies.stream()
                .map(c -> new DesiredOccupancy(c.roomCategory(), c.occupiedRooms(), c.potentialIncome()))
                .toList());
    }

}
