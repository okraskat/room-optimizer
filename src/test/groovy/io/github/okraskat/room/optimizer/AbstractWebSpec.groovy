package io.github.okraskat.room.optimizer

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.spockframework.spring.EnableSharedInjection
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import spock.lang.Shared
import spock.lang.Specification

@EnableSharedInjection
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractWebSpec extends Specification {

    @Shared
    @LocalServerPort
    int serverPort

    def setupSpec() {
        RestAssured.port = serverPort
    }

    def jsonRequest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
    }

}
