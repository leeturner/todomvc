package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.views.View
import java.net.URI

@Controller
internal class TodoItemController(
  private val repository: TodoItemRepository,
  private val todoItemMapper: TodoItemMapper
) {
  @View("index")
  @Get
  fun index(): Map<String, Any> {
    return createModel(ListFilter.ALL)
  }

  @View("index")
  @Get("/active")
  fun indexActive(): Map<String, Any> {
    return createModel(ListFilter.ACTIVE)
  }


  @Get("/completed")
  @View("index")
  fun indexCompleted(): Map<String, Any> {
    return createModel(ListFilter.COMPLETED)
  }

  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Post("/save")
  fun addNewTodoItem(@Body formData: TodoItemFormData?): HttpResponse<*> {
    repository.save(todoItemMapper.toEntity(formData))
    return HttpResponse.seeOther<Any>(ROOT)
  }

  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Post("/{id}/toggle")
  fun toggleSelection(@PathVariable id: Long?): HttpResponse<*> {
    repository.updateCompletedById(true, id)
    return HttpResponse.seeOther<Any>(ROOT)
  }

  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Post("/toggle-all")
  fun toggleAll(): HttpResponse<*> {
    repository.updateCompleted(true)
    return HttpResponse.seeOther<Any>(ROOT)
  }

  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Post("/{id}/delete")
  fun deleteTodoItem(@PathVariable id: Long): HttpResponse<*> {
    repository.deleteById(id)
    return HttpResponse.seeOther<Any>(ROOT)
  }

  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Post("/completed/delete")
  fun deleteCompletedItems(): HttpResponse<*> {
    val items = repository.findAllByCompleted(true)
    for ((id) in items) {
      repository.deleteById(id)
    }
    return HttpResponse.seeOther<Any>(ROOT)
  }

  private fun createModel(listFilter: ListFilter): Map<String, Any> {
    return java.util.Map.of(
      MODEL_ITEM, TodoItemFormData(),
      MODEL_TODOS, getTodoItems(listFilter),
      MODEL_TOTAL_NUMBER_OF_ITEMS, repository.count(),
      MODEL_NUMBER_OF_ACTIVE_ITEMS, numberOfActiveItems,
      MODEL_NUMBER_OF_COMPLETED_ITEMS, numberOfCompletedItems,
      MODEL_FILTER, listFilter
    )
  }

  private val numberOfActiveItems: Int
    get() = repository.countByCompleted(false)

  private val numberOfCompletedItems: Int
    get() = repository.countByCompleted(true)

  private fun getTodoItems(filter: ListFilter): List<TodoItem> {
    return when (filter) {
      ListFilter.ALL -> repository.findAll()
      ListFilter.ACTIVE -> repository.findAllByCompleted(false)
      ListFilter.COMPLETED -> repository.findAllByCompleted(true)
    }
  }

  enum class ListFilter {
    ALL,
    ACTIVE,
    COMPLETED
  }

  companion object {
    private const val MODEL_ITEM = "item"
    private const val MODEL_FILTER = "filter"
    private const val MODEL_TODOS = "todos"
    private const val MODEL_TOTAL_NUMBER_OF_ITEMS = "totalNumberOfItems"
    private const val MODEL_NUMBER_OF_ACTIVE_ITEMS = "numberOfActiveItems"
    private const val MODEL_NUMBER_OF_COMPLETED_ITEMS = "numberOfCompletedItems"
    private val ROOT: URI = URI.create("/")
  }
}
