package com.crm.crm.repository;


import com.crm.crm.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByCustomerId(Long customerId);
}
