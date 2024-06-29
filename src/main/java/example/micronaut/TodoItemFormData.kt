package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
open class TodoItemFormData {
  var title: @NotBlank String? = null
}
