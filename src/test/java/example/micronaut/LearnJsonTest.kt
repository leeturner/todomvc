package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
internal class LearnJsonTest {
  @Test
  fun learnJsonAvailable(@Client("/") httpClient: HttpClient) {
    val client = httpClient.toBlocking()
    Assertions.assertDoesNotThrow<HttpResponse<Any?>> { client.exchange("/learn.json") }
  }
}
