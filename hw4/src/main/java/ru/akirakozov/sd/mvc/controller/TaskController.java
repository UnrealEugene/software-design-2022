package ru.akirakozov.sd.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.akirakozov.sd.mvc.model.Task;
import ru.akirakozov.sd.mvc.model.TaskList;
import ru.akirakozov.sd.mvc.service.TaskService;

@RequiredArgsConstructor
@Controller
public class TaskController {
    private final TaskService taskService;

    @GetMapping({"/", ""})
    public String getTasks(Model model) {
        model.addAttribute("taskLists", taskService.findAllTaskLists());
        return "Index";
    }

    @PostMapping("/createTaskList")
    public String createTaskList(@ModelAttribute TaskList taskList) {
        taskService.createTaskList(taskList);
        return "redirect:/";
    }

    @PostMapping("/deleteTaskList")
    public String deleteTaskList(@ModelAttribute TaskList taskList) {
        taskService.deleteTaskList(taskList);
        return "redirect:/";
    }

    @GetMapping("/taskList")
    public String getTasksByTaskList(@RequestParam long id, Model model) {
        return prepareTaskListModel(id, model);
    }

    @PostMapping("/createTask")
    public String createTask(@ModelAttribute Task task, @RequestParam long taskListId, Model model) {
        taskService.createTask(task, taskListId);
        return prepareTaskListModel(taskListId, model);
    }

    @PostMapping("/deleteTask")
    public String deleteTask(@ModelAttribute Task task, @RequestParam long taskListId, Model model) {
        taskService.deleteTask(task);
        return prepareTaskListModel(taskListId, model);
    }

    @PostMapping("/completeTask")
    public String completeTask(@ModelAttribute Task task, @RequestParam long taskListId, Model model) {
        taskService.completeTask(task);
        return prepareTaskListModel(taskListId, model);
    }

    public String prepareTaskListModel(long id, Model model) {
        model.addAttribute("taskList", taskService.findTaskListById(id));
        return "TaskList";
    }
}
