package org.example.xhc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.xhc.entity.subject;

import java.util.List;

/**
 * cms_subject
 * @author xiaohongchao
 * @since 1.0.0
 */
@Mapper
public interface subjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(subject row);

    subject selectByPrimaryKey(Long id);

    List<subject> selectAll();

    int updateByPrimaryKey(subject row);
}