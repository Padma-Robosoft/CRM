package com.crm.crm.controller;


import com.crm.crm.dto.request.NoteRequestDTO;
import com.crm.crm.dto.response.APIResponseDTO;
import com.crm.crm.dto.response.NoteResponseDTO;
import com.crm.crm.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping("/{customerId}")
    public ResponseEntity<APIResponseDTO<String>> addNote(@PathVariable Long customerId, @RequestBody NoteRequestDTO requestDTO) {
        return noteService.addNote(customerId, requestDTO);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<APIResponseDTO<List<NoteResponseDTO>>> getNotesByCustomerId(@PathVariable Long customerId) {
        return noteService.getNotesByCustomerId(customerId);
    }

    @GetMapping
    public ResponseEntity<APIResponseDTO<List<NoteResponseDTO>>> getAllNotes() {
        return noteService.getAllNotes();
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<APIResponseDTO<String>> updateNoteById(@PathVariable Long noteId, @RequestBody NoteRequestDTO requestDTO) {
        return noteService.updateNoteById(noteId, requestDTO);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<APIResponseDTO<String>> deleteNoteById(@PathVariable Long noteId) {
        return noteService.deleteNoteById(noteId);
    }
}
