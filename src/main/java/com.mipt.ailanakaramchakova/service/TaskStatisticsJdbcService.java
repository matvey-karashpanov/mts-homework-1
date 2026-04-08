package com.mipt.ailanakaramchakova.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Service for task statistics using JdbcTemplate. Demonstrates direct JDBC access alongside JPA.
 */
@Service
public class TaskStatisticsJdbcService {

  private final JdbcTemplate jdbcTemplate;

  public TaskStatisticsJdbcService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Returns count of tasks grouped by priority. Uses JdbcTemplate with RowMapper.
   */
  public List<Map<String, Object>> getTasksCountByPriority() {
    String sql = """
      SELECT priority, COUNT(*) as count
      FROM tasks
      GROUP BY priority
      ORDER BY priority
      """;

    return jdbcTemplate.queryForList(sql);
  }

  /**
   * Alternative implementation with custom RowMapper.
   */
  public List<PriorityCountDto> getTasksCountByPriorityWithRowMapper() {
    String sql = """
      SELECT priority, COUNT(*) as count
      FROM tasks
      GROUP BY priority
      ORDER BY priority
      """;

    RowMapper<PriorityCountDto> rowMapper = new RowMapper<PriorityCountDto>() {
      @Override
      public PriorityCountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        PriorityCountDto dto = new PriorityCountDto();
        dto.setPriority(rs.getString("priority"));
        dto.setCount(rs.getLong("count"));
        return dto;
      }
    };

    return jdbcTemplate.query(sql, rowMapper);
  }

  /**
   * DTO for priority count statistics.
   */
  public static class PriorityCountDto {

    private String priority;
    private Long count;

    public String getPriority() {
      return priority;
    }

    public void setPriority(String priority) {
      this.priority = priority;
    }

    public Long getCount() {
      return count;
    }

    public void setCount(Long count) {
      this.count = count;
    }
  }
}
