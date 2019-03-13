package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<TimeEntry> rowMapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );
    private ResultSetExtractor<TimeEntry> rsExtractor = (rs) -> rs.next() ? rowMapper.mapRow(rs, 1) : null;

    @Autowired
    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        String sql = "INSERT INTO time_entries(project_id, user_id, date, hours) VALUES(?, ?, ?, ?)";
        PreparedStatementCreator psc = (con) -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, Date.valueOf(timeEntry.getDate()));
            ps.setInt(4, timeEntry.getHours());
            return ps;
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long id) {
        String sql = "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[] { id }, rsExtractor);
    }

    @Override
    public List<TimeEntry> list() {
        String sql = "SELECT id, project_id, user_id, date, hours FROM time_entries";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String sql = "UPDATE time_entries SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ?";
        jdbcTemplate.update(sql, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours(), id);
        return find(id);
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM time_entries WHERE id = ?";
        jdbcTemplate.update(sql, new Object[] { id });
    }
}
