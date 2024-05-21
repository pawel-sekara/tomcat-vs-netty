package dev.sekara.block.mvc.controller

import dev.sekara.block.domain.controller.BlockingNoteController
import dev.sekara.block.domain.rest.NoteDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notes")
class SpringNotesController(
    private val noteController: BlockingNoteController
) {

    @GetMapping
    fun getLastNotes(@RequestParam(required = false) limit: Int? = null): List<NoteDto> {
        val notes = limit?.let { noteController.getLast(it) } ?: noteController.getAll()
        return notes
    }

    @PutMapping
    fun createNote(note: NoteDto): NoteDto {
        return noteController.create(note)
    }

}