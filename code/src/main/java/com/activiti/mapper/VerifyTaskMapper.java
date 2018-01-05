package com.activiti.mapper;

import com.activiti.pojo.user.VerifyTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerifyTaskMapper {
    /**
     * 插入任务
     *
     * @param verifyTask
     * @return
     */
    int insertTask(VerifyTask verifyTask);

    /**
     * 更新成绩
     *
     * @param verifyTask
     * @return
     */
    int updateTask(VerifyTask verifyTask);

    /**
     * 查询任务
     *
     * @param status
     * @return
     */
    List<VerifyTask> selectAllTask(@Param("status") String status, @Param("offset") long offset, @Param("limit") int limit);

    /**
     * 查询任务数量
     * @param status
     * @return
     */
    long countAllTask(String status);
}
