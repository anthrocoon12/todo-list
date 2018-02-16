package models;

import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class ToDoRepositoryImpl implements ToDoRepository {
    private final JPAApi jpaApi;

    @Inject
    public ToDoRepositoryImpl(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public void insert(ToDo toDo) {
        EntityManager em = jpaApi.em();
        em.persist(toDo);
    }

    public void edit(ToDo toDo) {
        EntityManager em = jpaApi.em();
        ToDo dbToDo = em.find(ToDo.class, toDo.id);
        if (dbToDo == null) {
            return;
        }
        dbToDo.setTitle(toDo.getTitle());
        dbToDo.setTask(toDo.getTask());
    }

    public void delete(Long id) {
        EntityManager em = jpaApi.em();
        ToDo toDo = em.find(ToDo.class, id);
        System.out.println(toDo);
        if (toDo == null) {
            return;
        }
        toDo.setStatus(false);
    }

    public void complete(Long id) {
        EntityManager em = jpaApi.em();
        ToDo toDo = em.find(ToDo.class, id);
        if (toDo == null) {
            return;
        }
        toDo.setComplete(true);
    }

    public ToDo findById(Long id) {
        EntityManager em = jpaApi.em();
        ToDo toDo = em.find(ToDo.class, id);
        return toDo;
    }

    public List<ToDo> search(String title, String task, String author) {
        EntityManager em = jpaApi.em();
        title = title.toLowerCase();
        task = task.toLowerCase();
        author = author.toLowerCase();
        List<ToDo> toDoList = em.createQuery(
                "SELECT t FROM ToDo t " +
                        "WHERE LOWER(t.title) LIKE CONCAT('%',:title,'%') " +
                        "AND LOWER(t.task) LIKE CONCAT('%',:task,'%')" +
                        "AND LOWER(t.author) LIKE CONCAT('%',:author,'%')" +
                        "AND t.status = true")
                .setParameter("title", title)
                .setParameter("task", task)
                .setParameter("author", author)
                .getResultList();
        return toDoList;
    }

    public List<ToDo> findAll() {
        EntityManager em = jpaApi.em();
        List<ToDo> toDoList = em.createQuery("SELECT t FROM ToDo t WHERE t.status = true", ToDo.class)
                .getResultList();
        return toDoList;
    }

    public List<ToDo> findAllOrderByIdDesc() {
        EntityManager em = jpaApi.em();
        List<ToDo> toDoList = em.createQuery("SELECT t FROM ToDo t WHERE t.status = true ORDER BY t.id DESC", ToDo.class)
                .getResultList();
        return toDoList;
    }
}
