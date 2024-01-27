package com.example.webt.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.webt.entity.Webt;
import com.example.webt.form.WebtQuery;

public interface WebtDao {
    // JPQL による検索
    List<Webt> findByJPQL(WebtQuery webtQuery);
    // Criteria API による検索
    Page<Webt> findByCriteria(WebtQuery webtQuery, Pageable pageable); 
}
