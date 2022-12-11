package ru.akirakozov.sd.mvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Getter
@Setter
@Entity
@Table
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(mappedBy = "taskList", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Task> tasks;
}
