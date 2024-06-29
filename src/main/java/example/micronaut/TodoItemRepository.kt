package example.micronaut

import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.H2)
interface TodoItemRepository : CrudRepository<TodoItem, Long> {
  fun countByCompleted(completed: Boolean): Int

  fun findAllByCompleted(completed: Boolean): List<TodoItem>
  @Query("UPDATE todo_item SET completed = :completed")
  fun updateCompleted(completed: Boolean)

  @Query("UPDATE todo_item SET completed = :completed WHERE id = :id")
  fun updateCompletedById(completed: Boolean, id: Long?)
}
