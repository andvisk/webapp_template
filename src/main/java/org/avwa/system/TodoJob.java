package org.avwa.system;

import java.util.function.Consumer;

public class TodoJob<T> {

    private Consumer<T> todo;
    private Class<T> clazz;
    private String description;


    public TodoJob(Consumer<T> todo, Class<T> clazz, String description) {
        this.todo = todo;
        this.clazz = clazz;
        this.description = description;
    }

    public Consumer<T> getTodo() {
        return todo;
    }

    public Class<T> getClazz(){
        return clazz;
    }

    public String getDescription() {
        return description;
    }

}
