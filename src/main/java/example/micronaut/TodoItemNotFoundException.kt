package example.micronaut

class TodoItemNotFoundException(id: Long?) : RuntimeException(String.format("TodoItem with id %s not found", id))
