package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.views.View;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Controller
class TodoItemController {
    private static final String MODEL_ITEM = "item";
    private static final String MODEL_FILTER = "filter";
    private static final String MODEL_TODOS = "todos";
    private static final String MODEL_TOTAL_NUMBER_OF_ITEMS = "totalNumberOfItems";
    private static final String MODEL_NUMBER_OF_ACTIVE_ITEMS = "numberOfActiveItems";
    private static final String MODEL_NUMBER_OF_COMPLETED_ITEMS = "numberOfCompletedItems";
    private static final URI ROOT = URI.create("/");

    private final TodoItemRepository repository;
    private final TodoItemMapper todoItemMapper;

    TodoItemController(TodoItemRepository repository,
                       TodoItemMapper todoItemMapper) {
        this.repository = repository;
        this.todoItemMapper = todoItemMapper;
    }

    @View("index")
    @Get
    Map<String, Object> index() {
        return createModel(ListFilter.ALL);
    }

    @View("index")
    @Get("/active")
    Map<String, Object> indexActive() {
        return createModel(ListFilter.ACTIVE);
    }


    @Get("/completed")
    @View("index")
    Map<String, Object> indexCompleted() {
        return createModel(ListFilter.COMPLETED);
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/save")
    HttpResponse<?> addNewTodoItem(@Body TodoItemFormData formData) {
        repository.save(todoItemMapper.toEntity(formData));
        return HttpResponse.seeOther(ROOT);
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/{id}/toggle")
    HttpResponse<?> toggleSelection(@PathVariable Long id) {
        repository.updateCompletedById(true, id);
        return HttpResponse.seeOther(ROOT);
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/toggle-all")
    HttpResponse<?> toggleAll() {
        repository.updateCompleted(true);
        return HttpResponse.seeOther(ROOT);
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/{id}/delete")
    HttpResponse<?> deleteTodoItem(@PathVariable Long id) {
        repository.deleteById(id);
        return HttpResponse.seeOther(ROOT);
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/completed/delete")
    HttpResponse<?> deleteCompletedItems() {
        List<TodoItem> items = repository.findAllByCompleted(true);
        for (TodoItem item : items) {
            repository.deleteById(item.id());
        }
        return HttpResponse.seeOther(ROOT);
    }

    private Map<String, Object> createModel(ListFilter listFilter) {
        return Map.of(
                MODEL_ITEM, new TodoItemFormData(),
        MODEL_TODOS, getTodoItems(listFilter),
        MODEL_TOTAL_NUMBER_OF_ITEMS, repository.count(),
        MODEL_NUMBER_OF_ACTIVE_ITEMS, getNumberOfActiveItems(),
        MODEL_NUMBER_OF_COMPLETED_ITEMS, getNumberOfCompletedItems(),
        MODEL_FILTER, listFilter);
    }

    private int getNumberOfActiveItems() {
        return repository.countByCompleted(true);
    }

    private int getNumberOfCompletedItems() {
        return repository.countByCompleted(true);
    }

    private List<TodoItem> getTodoItems(ListFilter filter) {
        return switch (filter) {
            case ALL -> repository.findAll();
            case ACTIVE -> repository.findAllByCompleted(false);
            case COMPLETED -> repository.findAllByCompleted(true);
        };
    }

    public enum ListFilter {
        ALL,
        ACTIVE,
        COMPLETED
    }
}
