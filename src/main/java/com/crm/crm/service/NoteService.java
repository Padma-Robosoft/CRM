package com.crm.crm.service;


import com.crm.crm.dto.request.NoteRequestDTO;
import com.crm.crm.dto.response.APIResponseDTO;
import com.crm.crm.dto.response.NoteResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {

    ResponseEntity<APIResponseDTO<String>> addNote(Long customerId, NoteRequestDTO noteRequest);
    ResponseEntity<APIResponseDTO<List<NoteResponseDTO>>> getNotesByCustomerId(Long customerId);
    ResponseEntity<APIResponseDTO<String>> deleteNoteById(Long noteId);
    ResponseEntity<APIResponseDTO<List<NoteResponseDTO>>> getAllNotes();
    ResponseEntity<APIResponseDTO<String>> updateNoteById(Long noteId, NoteRequestDTO requestDTO);
}
