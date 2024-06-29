package org.jeecg.modules.estar.tw.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwTaskMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Description: 项目任务团队表
 * @Author: nbacheng
 * @Date:   2023-07-03
 * @Version: V1.0
 */
@SuppressWarnings("rawtypes")
public interface TwTaskMemberMapper extends BaseMapper<TwTaskMember> {
	@Select("SELECT * FROM tw_task_member WHERE task_id = #{taskId} ORDER BY is_owner")
    IPage<Map> selectTaskMemberByTaskId(IPage<Map> page, @Param("taskId") String taskId);
}
