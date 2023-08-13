package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepository extends CrudRepository<Holiday,String> {
 /*   private final JdbcTemplate jdbcTemplate;
    @Autowired
    public HolidayRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Holiday> findAllHoliday(){
        String sql="SELECT * FROM holidays";
        var rowMapper=BeanPropertyRowMapper.newInstance(Holiday.class);
        return jdbcTemplate.query(sql,rowMapper);
    }*/
}
