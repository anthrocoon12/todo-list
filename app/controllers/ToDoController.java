package controllers;

import models.ToDo;
import models.ToDoRepository;
import models.ToDoSearch;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.defaultpages.todo;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class ToDoController extends Controller{
    private final FormFactory formFactory;
    private final ToDoRepository toDoRepository;

    @Inject
    public ToDoController(FormFactory formFactory, ToDoRepository toDoRepository) {
        this.formFactory = formFactory;
        this.toDoRepository = toDoRepository;
    }

    public Result mainpage() {
        List<ToDo> toDoList = toDoRepository.findAllOrderByIdDesc();
        Html content = views.html.todolist.apply(toDoList, "To-Do List");
        return ok(views.html.main.render("TDL : To-Do List web for Smart People", true, content));
    }

    public Result singleToDo(Long id) {
        ToDo toDo = toDoRepository.findById(id);
        if (toDo == null) {
            return redirect(routes.ToDoController.mainpage());
        } else {
            List<ToDo> toDoList = new ArrayList<>();
            toDoList.add(toDo);

            Html content = views.html.todolist.apply(toDoList, "");
            return ok(views.html.main.render("TDL : To-Do List web for Smart People", true, content));
        }
    }

    public Result search() {
        ToDoSearch toDoSearch = formFactory.form(ToDoSearch.class).bindFromRequest().get();
        String keyTitle, keyTask, keyAuthor;
        keyTitle = (toDoSearch.getTitle().equals("")) ? "" : toDoSearch.getTitle();
        keyTask = (toDoSearch.getTask().equals("")) ? "" : toDoSearch.getTask();
        keyAuthor = (toDoSearch.getAuthor().equals("")) ? "" : toDoSearch.getAuthor();
        List<ToDo> toDoList = toDoRepository.search(keyTitle, keyTask, keyAuthor);
        Html content = views.html.todolist.apply(toDoList, "Search Result");
        return ok(views.html.main.render("TDL : Search Result", true, content));
    }

    public Result addToDo() {
        ToDo toDo = formFactory.form(ToDo.class).bindFromRequest().get();
        if (toDo.getTitle().equals("")) toDo.setTitle("To-Do");
        if (toDo.getAuthor().equals("")) toDo.setAuthor("Anonymous");
        toDo.setComplete(false);
        toDo.setStatus(true);

        toDoRepository.insert(toDo);
        return redirect(routes.ToDoController.mainpage());
    }

    public Result showEditPage(Long id) {
        ToDo toDo = toDoRepository.findById(id);
        if (toDo == null) {
            return redirect(routes.ToDoController.mainpage());
        } else {
            Html content = views.html.edittodo.apply(toDo);
            return ok(views.html.main.render("TDL : Edit To-Do", false, content));
        }
    }

    public Result editToDo(Long id) {
        ToDo toDo = formFactory.form(ToDo.class).bindFromRequest().get();
        toDo.setId(id);
        if (toDo.getTitle().equals("")) toDo.setTitle("To-Do");
        toDoRepository.edit(toDo);
        return redirect(routes.ToDoController.mainpage());
    }

    public Result setComplete(Long id) {
        toDoRepository.complete(id);
        return redirect(routes.ToDoController.mainpage());
    }

    public Result deleteToDo(Long id) {
        toDoRepository.delete(id);
        return redirect(routes.ToDoController.mainpage());
    }
}
