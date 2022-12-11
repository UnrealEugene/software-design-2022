package ru.akirakozov.sd.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akirakozov.sd.mvc.model.Task;
import ru.akirakozov.sd.mvc.model.TaskList;
import ru.akirakozov.sd.mvc.repository.TaskListRepository;
import ru.akirakozov.sd.mvc.repository.TaskRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public List<TaskList> findAllTaskLists() {
        return taskListRepository.findAll();
    }

    public void createTaskList(TaskList taskList) {
        taskListRepository.save(taskList);
    }

    public void deleteTaskList(TaskList taskList) {
        taskListRepository.deleteById(taskList.getId());
    }

    public TaskList findTaskListById(long id) {
        return taskListRepository.findById(id).orElse(null);
    }

    public void createTask(Task task, long taskListId) {
        TaskList taskList = new TaskList();
        taskList.setId(taskListId);
        task.setTaskList(taskList);
        taskRepository.save(task);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    public void completeTask(Task task) {
        taskRepository.setCompletedForTask(task.getId());
    }
}
