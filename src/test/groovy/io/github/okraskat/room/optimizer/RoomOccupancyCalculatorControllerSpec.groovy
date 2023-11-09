package io.github.okraskat.room.optimizer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.okraskat.room.optimizer.domain.RoomCategory

import io.github.okraskat.room.optimizer.http.api.OccupancyCalculationRequest
import io.github.okraskat.room.optimizer.http.api.OccupancyCalculationResponse
import org.springframework.http.HttpStatus
import spock.lang.Shared

class RoomOccupancyCalculatorControllerSpec extends AbstractWebSpec {

    @Shared
    def potentialPayments

    @Shared
    def objectMapper = new ObjectMapper()

    def setupSpec() {
        def stream = this.class.getResourceAsStream("/mock-data/test-clients.json")
        potentialPayments = objectMapper.readValue(stream, new TypeReference<List<Integer>>() {})
    }

    def "should calculate properly rooms occupancy for provided payments set"() {
        given:
        def calculationRequest = new OccupancyCalculationRequest(availableRooms, potentialPayments)

        when:
        def response = jsonRequest()
                .body(calculationRequest)
                .post("/occupancy-calculations")

        then:
        response.statusCode() == HttpStatus.OK.value()
        def desiredOccupancies = response.as(OccupancyCalculationResponse.class).desiredOccupancies()
        desiredOccupancies.forEach {
            assert it.usedRooms() == expectedOccupiedRooms[it.roomCategory()]
            assert it.potentialProfit() == expectedProfit[it.roomCategory()]
        }

        where:
        availableRooms                                          || expectedOccupiedRooms                                  || expectedProfit
        [(RoomCategory.PREMIUM): 3, (RoomCategory.ECONOMY): 3]  || [(RoomCategory.PREMIUM): 3, (RoomCategory.ECONOMY): 3] || [(RoomCategory.PREMIUM): 738, (RoomCategory.ECONOMY): 167]
        [(RoomCategory.PREMIUM): 7, (RoomCategory.ECONOMY): 5]  || [(RoomCategory.PREMIUM): 6, (RoomCategory.ECONOMY): 4] || [(RoomCategory.PREMIUM): 1054, (RoomCategory.ECONOMY): 189]
        [(RoomCategory.PREMIUM): 2, (RoomCategory.ECONOMY): 7]  || [(RoomCategory.PREMIUM): 2, (RoomCategory.ECONOMY): 4] || [(RoomCategory.PREMIUM): 583, (RoomCategory.ECONOMY): 189]
        [(RoomCategory.PREMIUM): 10, (RoomCategory.ECONOMY): 1] || [(RoomCategory.PREMIUM): 7, (RoomCategory.ECONOMY): 1] || [(RoomCategory.PREMIUM): 1153, (RoomCategory.ECONOMY): 45]
        [(RoomCategory.ECONOMY): 2]                             || [(RoomCategory.ECONOMY): 2]                            || [(RoomCategory.ECONOMY): 144]
        [(RoomCategory.PREMIUM): 2]                             || [(RoomCategory.PREMIUM): 2]                            || [(RoomCategory.PREMIUM): 583]
        [(RoomCategory.PREMIUM): 0]                             || [(RoomCategory.PREMIUM): 0]                            || [(RoomCategory.PREMIUM): 0]
        [(RoomCategory.ECONOMY): 0]                             || [(RoomCategory.ECONOMY): 0]                            || [(RoomCategory.ECONOMY): 0]
    }

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
        [(RoomCategory.PREMIUM):1]                     || []
        [(RoomCategory.PREMIUM):1]                     || null
        Collections.emptyMap()                         || [BigDecimal.ONE]
        null                                           || [BigDecimal.ONE]
    }

}
