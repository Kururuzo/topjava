package ru.javawebinar.topjava;

import java.time.LocalDateTime;

public interface HasDateTime extends HasId{
    LocalDateTime getDateTime();
}
