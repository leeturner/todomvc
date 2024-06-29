package example.micronaut

import io.micronaut.context.annotation.Mapper
import jakarta.inject.Singleton

@Singleton
interface TodoItemMapper {
  @Mapper.Mapping(to = "completed", from = "#{false}")
  fun toEntity(form: TodoItemFormData?): TodoItem?
}
