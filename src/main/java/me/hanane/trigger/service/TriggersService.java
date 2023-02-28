package me.hanane.trigger.service;

import me.hanane.trigger.Trigger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TriggersService {

    private final TriggersRepository repository;

    public TriggersService(TriggersRepository repository) {
        this.repository = repository;
    }

    public List<Trigger> fetch() {
        return repository.findAll();
    }

    public Optional<Trigger> get(Long id) {
        return repository.findById(id);
    }

    public Trigger save(Trigger trigger) {
        return repository.save(trigger);
    }

    public Trigger update(Trigger entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void delete(Trigger trigger) {
        repository.delete(trigger);
    }

    public Page<Trigger> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Trigger> list(Pageable pageable, Specification<Trigger> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
