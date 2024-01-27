package com.example.webt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.webt.entity.Webt;

//error:不要な `@Repository`
@Repository
public interface WebtRepository extends JpaRepository<Webt, Integer> { 
	
    List<Webt> findByTitleLike(String title);
    List<Webt> findByAuthorLike(String author);
    List<Webt> findByStartYearBetweenOrderByStartYearAsc(Integer from, Integer to);
    List<Webt> findByStartYearGreaterThanEqualOrderByStartYearAsc(Integer from);
    List<Webt> findByStartYearLessThanEqualOrderByStartYearAsc(Integer to);
    List<Webt> findByDone(String done);
}
