package example.micronaut

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
internal class TodoItemMapperTest {
  @Test
  fun mapper(mapper: TodoItemMapper) {
    val todoItem = mapper.toEntity(object : TodoItemFormData() {
      init {
        title = "Micronaut"
      }
    })
    Assertions.assertNotNull(todoItem)
    Assertions.assertNull(todoItem!!.id)
    Assertions.assertEquals("Micronaut", todoItem.title)
    Assertions.assertFalse(todoItem.completed)
  }
}