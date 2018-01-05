package com.activiti.mapper;

import com.activiti.pojo.tools.EmailLog;
import com.activiti.pojo.tools.InvokeLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolsMapper {

    int insertInvokeLog(InvokeLog invokeLog);

    List<InvokeLog> selectInvokeLog(@Param("offset") long offset, @Param("limit") int limit);

    long countInvokeLog();

    long countEmailLog();

    int insertEmailLog(EmailLog emailLog);

    List<EmailLog> selectEmailLog(@Param("offset") long offset, @Param("limit") int limit);
}
