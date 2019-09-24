package com.gjb.demo.dao;

import com.gjb.demo.model.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by limi on 2017/10/16.
 */
public interface TagRepository extends JpaRepository<Tag,Long> {

    Tag findByName(String name);
    @Query("select t from Tag t")
    List<Tag> findtop(Pageable pageable);
}
