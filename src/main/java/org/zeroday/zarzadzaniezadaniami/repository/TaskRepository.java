package org.zeroday.zarzadzaniezadaniami.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zeroday.zarzadzaniezadaniami.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
