package net.santhosh.todo.service;

import lombok.AllArgsConstructor;
import net.santhosh.todo.dto.TodoDto;
import net.santhosh.todo.entity.Todo;
import net.santhosh.todo.exception.ResourceNotFoundException;
import net.santhosh.todo.repository.TodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;

    private ModelMapper modelMapper;

    @Override
    public TodoDto addTodo(TodoDto todoDto) {
        //Convert TodoDto into JPA Entity;
//        Todo todo = new Todo();
//        todo.setId(todoDto.getId());
//        todo.setTitle(todoDto.getTitle());
//        todo.setDescription(todoDto.getDescription());
//        todo.setCompleted(todoDto.isCompleted());

        Todo todo = modelMapper.map(todoDto, Todo.class);

        // Todo JPA Entity and save into db
        Todo savedTodo = todoRepository.save(todo);

        // Convert SavedTodo todo JPA entity object into TodoDto Object

//        TodoDto savedTodoDto = new TodoDto();
//        savedTodoDto.setId(savedTodo.getId());
//        savedTodoDto.setTitle(savedTodo.getTitle());
//        savedTodoDto.setDescription(savedTodo.getDescription());
//        savedTodoDto.setCompleted(savedTodo.isCompleted());

        TodoDto savedTodoDto = modelMapper.map(savedTodo, TodoDto.class);

        return savedTodoDto;
    }

    @Override
    public TodoDto getTodo(Long id) {
       Todo todo = todoRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Todo Not Found with id " + id));
       // Convert todo into TodoDto
        TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
        return todoDto;
    }

    @Override
    public List<TodoDto> getAllTodos() {
        List<Todo> todoList = todoRepository.findAll();
        return todoList.stream().map((todo) -> modelMapper.map(todo, TodoDto.class)).collect(Collectors.toList());
    }

    @Override
    public TodoDto updateTodo(TodoDto todoDto, Long id) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo Not Found with id " + id));
        existingTodo.setTitle(todoDto.getTitle());
        existingTodo.setDescription(todoDto.getDescription());
        existingTodo.setCompleted(todoDto.isCompleted());
        Todo updatedTodo = todoRepository.save(existingTodo);
        // convert todo into TodoDto
        return modelMapper.map(updatedTodo, TodoDto.class);
    }

    @Override
    public void deleteTodo(Long id) {
       Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + id));
       todoRepository.deleteById(id);
    }

    @Override
    public TodoDto completeTodo(Long id) {
       Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo Not found with id " + id));
       todo.setCompleted(Boolean.TRUE);
       Todo updatedTodo = todoRepository.save(todo);
       return modelMapper.map(updatedTodo, TodoDto.class);
    }

    @Override
    public TodoDto inCompleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo Not found with id " + id));
        todo.setCompleted(Boolean.FALSE);
        todoRepository.save(todo);
        return modelMapper.map(todo, TodoDto.class);
    }
}
