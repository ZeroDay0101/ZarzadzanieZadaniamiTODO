package org.zeroday.zarzadzaniezadaniami.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Zadanie o id " + id + " nie zostało znalezione");
    }
}
