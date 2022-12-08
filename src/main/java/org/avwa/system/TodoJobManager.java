package org.avwa.system;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import org.slf4j.Logger;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Stateless
public class TodoJobManager {

    @Inject
    Logger log;

    @Inject
    ApplicationEJB applicationEJB;

    @Inject
    BeanManager beanManager;

    public <T> void doTheJob(TodoJob todoJob) {
        Bean<?> bean = beanManager.resolve(beanManager.getBeans(todoJob.getClazz()));
        CreationalContext<?> context = beanManager.createCreationalContext(bean);
        T runBean = (T) beanManager.getReference(bean, todoJob.getClazz(), context);

        todoJob.getTodo().accept(runBean);
    }

    public <T> void addTodoForAllSessions(Consumer<T> consumer, Class<T> clazz, String description) {
        TodoJob<T> todoJob = new TodoJob<>(consumer, clazz, description);
        for (Map.Entry<String, HttpSessionInfo> entry : applicationEJB.getHttpsessions().entrySet()) {
            addTodoForSession(entry.getKey(), todoJob);
        }
    }

    public <T> void addTodoForSession(String sessionId, TodoJob<T> todoJob) {
        Queue<TodoJob> queue = applicationEJB.getTodoforsessions().get(sessionId);
        if (queue == null) {
            queue = new ConcurrentLinkedQueue<TodoJob>();
            applicationEJB.getTodoforsessions().put(sessionId, queue);
        }
        queue.add(todoJob);
    }

    public void doJobsForSession(String sessionId) {
        Queue<TodoJob> queue = applicationEJB.getTodoforsessions().get(sessionId);
        if (queue != null) {
            TodoJob job = queue.poll();
            while (job != null) {
                log.debug("running todoJob for session:" + sessionId + ", description:" + job.getDescription());

                doTheJob(job);
                job = queue.poll();
            }
        }
    }
}
