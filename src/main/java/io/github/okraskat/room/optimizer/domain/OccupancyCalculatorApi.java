package io.github.okraskat.room.optimizer.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OccupancyCalculatorApi {

    List<CalculatedOccupancy> calculate(Map<RoomCategory, Integer> availableRoomsPerCategory,
                                        Collection<BigDecimal> potentialPayments);


}
