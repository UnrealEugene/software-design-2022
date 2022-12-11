package ru.akirakozov.sd.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.akirakozov.sd.mvc.model.TaskList;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {
}
