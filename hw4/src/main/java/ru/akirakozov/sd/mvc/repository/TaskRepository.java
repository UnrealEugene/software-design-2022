package ru.akirakozov.sd.mvc.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.akirakozov.sd.mvc.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Modifying
    @Transactional
    @Query("update Task set completed = true where id = ?1")
    void setCompletedForTask(long id);
}
