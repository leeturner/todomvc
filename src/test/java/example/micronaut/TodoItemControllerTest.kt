package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
internal class TodoItemControllerTest {
  @Test
  fun crud(@Client("/") httpClient: HttpClient, repository: TodoItemRepository) {
    val client = httpClient.toBlocking()
    // create
    val count = repository.count()
    val data = TodoItemFormData()
    data.title = "Micronaut"
    client.exchange<TodoItemFormData, Any>(
      HttpRequest.POST("/save", data).contentType(MediaType.APPLICATION_FORM_URLENCODED)
    )
    Assertions.assertEquals(count + 1, repository.count())
  }
}