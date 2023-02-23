package me.hanane.trigger.service;

import me.hanane.trigger.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TriggersRepository extends
        JpaRepository<Trigger, Long>,
        JpaSpecificationExecutor<Trigger> {
}
