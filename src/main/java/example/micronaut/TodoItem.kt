package example.micronaut

import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

@MappedEntity
@JvmRecord
data class TodoItem(
  @field:GeneratedValue @field:Id @param:Id val id: @Nullable Long?,
  val title: String,
  val completed: Boolean
)
