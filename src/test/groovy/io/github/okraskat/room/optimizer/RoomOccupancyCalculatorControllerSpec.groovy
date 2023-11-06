package io.github.okraskat.room.optimizer

import io.github.okraskat.room.optimizer.domain.RoomCategory
import io.github.okraskat.room.optimizer.http.api.AvailableRooms
import io.github.okraskat.room.optimizer.http.api.OccupancyCalculationRequest
import org.springframework.http.HttpStatus

class RoomOccupancyCalculatorControllerSpec extends AbstractWebSpec {

    def "should reject invalid calculation requests"() {
        given:
        def calculationRequest = new OccupancyCalculationRequest(availableRooms, payments)

        when:
        def response = jsonRequest()
                .body(calculationRequest)
                .post("/occupancy-calculations")

        then:
        response.statusCode() == HttpStatus.BAD_REQUEST.value()

        where:
        availableRooms                                 || payments
        [new AvailableRooms(RoomCategory.PREMIUM, 1)]  || []
        [new AvailableRooms(RoomCategory.PREMIUM, 1)]  || null
        [new AvailableRooms(RoomCategory.PREMIUM, 0)]  || [1]
        [new AvailableRooms(null, 1)]                  || [1]
        []                                             || [1]
        null                                           || [1]
    }

}
