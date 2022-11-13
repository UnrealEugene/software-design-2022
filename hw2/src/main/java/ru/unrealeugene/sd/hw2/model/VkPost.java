package ru.unrealeugene.sd.hw2.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class VkPost {
    private long id;
    private Date date;
    private String text;
}
