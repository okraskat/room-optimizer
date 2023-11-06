package io.github.okraskat.room.optimizer

import io.restassured.RestAssured
import org.spockframework.spring.EnableSharedInjection
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Shared
import spock.lang.Specification

@EnableSharedInjection
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControllerSpec extends Specification {

    @Shared
    @LocalServerPort
    int serverPort

    def setupSpec() {
        RestAssured.port = serverPort
    }

    def "should fetch greetings"() {
        when:
        def response = RestAssured.get("/greetings")
        then:
        response.statusCode() == HttpStatus.OK.value()
        response.path('message') == 'hello'
    }

}
