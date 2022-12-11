package ru.akirakozov.sd.mvc.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Nonnull
    private String name;

    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "task_list_id", nullable = false)
    private TaskList taskList;
}
