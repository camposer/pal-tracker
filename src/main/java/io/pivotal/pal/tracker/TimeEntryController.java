package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntry) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntry);
        return new ResponseEntity(createdTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry foundTimeEntry = timeEntryRepository.find(timeEntryId);
        if (foundTimeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(foundTimeEntry, HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntries = timeEntryRepository.list();
        return new ResponseEntity<>(timeEntries, HttpStatus.OK);
    }

    @PutMapping("{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry timeEntry) {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(timeEntryId, timeEntry);
        if (updatedTimeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(updatedTimeEntry, HttpStatus.OK);
        }
    }


    @DeleteMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
