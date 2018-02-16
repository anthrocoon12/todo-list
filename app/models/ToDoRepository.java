package models;

import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(ToDoRepositoryImpl.class)
public interface ToDoRepository {
    void insert(ToDo toDo);
    void edit(ToDo toDo);
    void delete(Long id);
    void complete(Long id);
    ToDo findById(Long id);
    List<ToDo> search(String title, String task, String author);
    List<ToDo> findAllOrderByIdDesc();
}
