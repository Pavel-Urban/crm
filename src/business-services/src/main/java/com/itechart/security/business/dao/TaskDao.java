package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.task.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public interface TaskDao {

    Task get(Long id);

    List<Task> loadAll();

    Long save(Task task);

    void update(Task convert);

    boolean delete(Long id);

    boolean delete(Task task);
}
