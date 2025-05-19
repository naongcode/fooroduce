package com.foodu.Event.Service;

import com.foodu.Event.Dto.EventRecommendResponse;
import com.foodu.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventRecommendService {

    private final JdbcTemplate jdbcTemplate;

    // 가까운순으로 3개까지 조회
    public List<EventRecommendResponse> findNearbyEvents(double longitude, double latitude, double radius) {
        String sql = """
            select * from fooroduce.event e
            where e.event_end >= now()
            and ST_DWithin(
                e.location_point,
                ST_SetSRID(ST_MakePoint(?, ?), 4326)::geography,
                ?
            )
            ORDER BY ST_Distance(
                e.location_point::geography,
                ST_SetSRID(ST_MakePoint(?, ?), 4326)::geography
            )
            LIMIT 3
            """;

        return jdbcTemplate.query(
                sql, this::mapRowToDto, longitude, latitude, radius, longitude, latitude);

    }

    private EventRecommendResponse mapRowToDto(ResultSet rs, int rowNum) throws SQLException {
        return EventRecommendResponse.builder()
                .eventId(rs.getInt("event_id"))
                .eventName(rs.getString("event_name"))
                .eventHost(rs.getString("event_host"))
                .eventImage(rs.getString("event_image"))
                .description(rs.getString("description"))
                .location(rs.getString("location"))
                .latitude(rs.getDouble("latitude"))
                .longitude(rs.getDouble("longitude"))
                .eventStart(rs.getTimestamp("event_start").toLocalDateTime())
                .eventEnd(rs.getTimestamp("event_end").toLocalDateTime())
                .build();
    }
}
