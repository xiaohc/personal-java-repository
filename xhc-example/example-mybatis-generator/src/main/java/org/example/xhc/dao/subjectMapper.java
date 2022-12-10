package org.example.xhc.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.example.xhc.entity.subject;

@Mapper
public interface subjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(subject row);

    subject selectByPrimaryKey(Long id);

    List<subject> selectAll();

    int updateByPrimaryKey(subject row);
}