package com.crm.crm.service.impl;

import com.crm.crm.dto.request.NoteRequestDTO;
import com.crm.crm.dto.response.APIResponseDTO;
import com.crm.crm.dto.response.NoteResponseDTO;
import com.crm.crm.exception.ResourceNotFoundException;
import com.crm.crm.model.Customer;
import com.crm.crm.model.Note;
import com.crm.crm.repository.CustomerRepository;
import com.crm.crm.repository.NoteRepository;
import com.crm.crm.service.NoteService;
import com.crm.crm.util.APIResponseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final CustomerRepository customerRepository;
    private static final Logger logger = LogManager.getLogger(NoteServiceImpl.class);

    @Value("${error.note.notfound}")
    private String noteNotFoundMessage;

    @Value("${error.note.emptyContent}")
    private String noteEmptyContentMessage;

    @Value("${response.noteAdded}")
    private String noteAddedMessage;

    @Value("${response.noteDeleted}")
    private String noteDeletedMessage;

    @Value("${error.customer.notfound}")
    private String customerNotFoundMessage;


    @Value("${response.note.updated}")
    private String noteUpdatedMessage;


    @Value("${log.note.add}")
    private String logAddNote;

    @Value("${log.note.getByCustomer}")
    private String logGetNotesByCustomer;

    @Value("${log.note.getAll}")
    private String logGetAllNotes;

    @Value("${log.note.update}")
    private String logUpdateNote;

    @Value("${log.note.delete}")
    private String logDeleteNote;

    @Value("${log.note.error.emptyContent}")
    private String logNoteEmptyContent;

    @Value("${log.note.error.notFound}")
    private String logNoteNotFound;

    @Value("${log.customer.error.notFound}")
    private String logCustomerNotFound;


    public NoteServiceImpl(NoteRepository noteRepository, CustomerRepository customerRepository) {
        this.noteRepository = noteRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ResponseEntity<APIResponseDTO<String>> addNote(Long customerId, NoteRequestDTO noteRequest) {
        if (noteRequest == null || noteRequest.getContent() == null || noteRequest.getContent().isEmpty()) {
            logger.error(logNoteEmptyContent);
            throw new ResourceNotFoundException(noteEmptyContentMessage);
        }
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->{ logger.error(logCustomerNotFound, customerId);
                    return new ResourceNotFoundException(String.format(customerNotFoundMessage, customerId));
                });


        logger.info(logAddNote, customerId);
        Note note=new Note();
        note.setContent(noteRequest.getContent());
        note.setTitle(noteRequest.getTitle());
        note.setCustomer(customer);
        noteRepository.save(note);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", noteAddedMessage);

        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponseUtil.createSuccessResponse(noteAddedMessage));
    }

    @Override
    public ResponseEntity<APIResponseDTO<List<NoteResponseDTO>>> getNotesByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.error(logCustomerNotFound, customerId);
                    return new ResourceNotFoundException(String.format(customerNotFoundMessage, customerId));
                });
        logger.info(logGetNotesByCustomer, customerId);
        List<Note> notes = noteRepository.findByCustomerId(customerId);
        if (notes.isEmpty()) {
            return ResponseEntity.ok(APIResponseUtil.successResponse(Collections.emptyList()));
        }
        List<NoteResponseDTO> response = notes.stream()
                .map(note -> new NoteResponseDTO(note.getId(), note.getTitle(), note.getContent()))
                .collect(Collectors.toList());
        Map<String, List<NoteResponseDTO>> responseData = new HashMap<>();
        responseData.put("notes", response);
        return ResponseEntity.ok(APIResponseUtil.successResponse(response));
    }

    @Override
    public ResponseEntity<APIResponseDTO<List<NoteResponseDTO>>> getAllNotes() {
        logger.info(logGetAllNotes);
        List<Note> notes = noteRepository.findAll();
        List<NoteResponseDTO> responseDTOs = notes.stream()
                .map(note -> new NoteResponseDTO(note.getId(), note.getTitle(), note.getContent()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(APIResponseUtil.successResponse(responseDTOs));
    }

    @Override
    public ResponseEntity<APIResponseDTO<String>> updateNoteById(Long noteId, NoteRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.getContent() == null || requestDTO.getContent().isEmpty()) {
            logger.error(logNoteEmptyContent);
            throw new ResourceNotFoundException(noteEmptyContentMessage);
        }
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> {
                    logger.error(logNoteNotFound, noteId);
                    return new ResourceNotFoundException(String.format(noteNotFoundMessage, noteId));
                });
        logger.info(logUpdateNote, noteId);
        note.setTitle(requestDTO.getTitle());
        note.setContent(requestDTO.getContent());
        noteRepository.save(note);
        return ResponseEntity.ok(APIResponseUtil.createSuccessResponse(noteUpdatedMessage));
    }


    @Override
    public ResponseEntity<APIResponseDTO<String>> deleteNoteById(Long noteId) {
     Note note = noteRepository.findById(noteId)
                .orElseThrow(() ->{
                    logger.error(logNoteNotFound, noteId);
                    return new ResourceNotFoundException(String.format(noteNotFoundMessage, noteId));
                });
        logger.info(logDeleteNote, noteId);
        noteRepository.delete(note);
        return ResponseEntity.ok(APIResponseUtil.createSuccessResponse(noteDeletedMessage));
    }

}