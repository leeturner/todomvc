package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import jakarta.inject.Singleton

@Produces
@Singleton
@Requires(classes = [TodoItemNotFoundException::class, ExceptionHandler::class])
internal class TodoItemNotFoundExceptionHandler(private val errorResponseProcessor: ErrorResponseProcessor<*>) :
  ExceptionHandler<TodoItemNotFoundException, HttpResponse<*>> {
  override fun handle(request: HttpRequest<*>?, e: TodoItemNotFoundException): HttpResponse<*> {
    return errorResponseProcessor.processResponse(
      ErrorContext.builder(request)
        .cause(e)
        .errorMessage(e.message)
        .build(), HttpResponse.notFound<Any>()
    )
  }
}