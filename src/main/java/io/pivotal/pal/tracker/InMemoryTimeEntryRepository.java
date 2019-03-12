package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Map<Long, TimeEntry> timeEntriesMap = new LinkedHashMap<>();
    private AtomicLong idSeq = new AtomicLong();

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        long id = idSeq.incrementAndGet();
        timeEntry.setId(id);
        timeEntriesMap.put(id, timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        return timeEntriesMap.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntriesMap.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry storedTimeEntry = timeEntriesMap.get(id);
        if (storedTimeEntry != null) {
            timeEntry.setId(id);
            timeEntriesMap.put(id, timeEntry);
            storedTimeEntry = timeEntry;
        }
        return storedTimeEntry;
    }

    @Override
    public void delete(long id) {
        timeEntriesMap.remove(id);
    }
}
