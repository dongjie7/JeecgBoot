package org.jeecg.modules.estar.tw.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import org.jeecg.modules.estar.tw.entity.TwTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Description: 项目任务表
 * @Author: nbacheng
 * @Date:   2023-07-01
 * @Version: V1.0
 */
public interface TwTaskMapper extends BaseMapper<TwTask> {
	@Select("SELECT * FROM tw_task WHERE id = #{id} LIMIT 1")
    Map selectTaskById(@Param("id") String id);
	
	@Select("SELECT * FROM tw_task WHERE version_id = #{params.versionId} and deleted = #{params.deleted}")
    List<Map> selectTaskListByVersionAndDelete(@Param("params")  Map params);
	
	@Select("select max(a.id_num) id_num from tw_task a where a.project_id = #{projectId}")
    Integer selectMaxIdNumByProjectId(@Param("projectId") String projectId);
	
	IPage<Map> selectTaskListByParam(IPage<Map> page,@Param("params") Map params);
	
	@Update("UPDATE tw_task SET `like` = #{like}  WHERE id = #{id}")
    Integer updateTaskLike(@Param("like") Integer like,@Param("id") String id);
    @Update("UPDATE tw_task SET `star` = #{star}  WHERE id = #{id}")
    Integer updateTaskStar(@Param("star") Integer star,@Param("id") String id);
    
    @Select("SELECT * FROM tw_task WHERE id = #{id} and deleted= 0 LIMIT 1")
    Map selectTaskByIdNoDel(@Param("id") String id);
    
    @Select("SELECT a.id,a.project_id,a.name,a.pri,a.execute_status,a.description,a.create_by,a.create_time,a.assign_to,a.deleted,a.stage_id,a.task_tag,a.done,a.begin_time,a.end_time,a.remind_time,a.pid,a.sort,a.liked,a.star,a.deleted_time,a.privated,a.id_num,a.path,a.schedule,a.version_id,a.features_id,a.work_time,a.status FROM tw_task a WHERE a.id = #{id} ")
    TwTask selTaskById(@Param("id") String id);
    
    @Update("UPDATE tw_task SET features_id = '' , version_id = '' WHERE features_id = #{featuresId}")
    Integer updateTaskFeaAndVerByFeaId(@Param("featuresId") String featuresId);
    
    @Update("UPDATE tw_task SET features_id = '' , version_id = '' WHERE version_id = #{versionId}")
    Integer updateTaskFeaAndVerByVerId(@Param("versionId") String versionId);
    
    @Select(" select count(a.id) id from tw_task a where a.project_id = #{projectId} and (a.create_time BETWEEN #{beginTime} and #{endTime}) ")
    Integer selectDateTaskTotalForProject(@Param("projectId") String projectId,@Param("beginTime") String beginTime, @Param("endTime")String endTime);
    
    @Select("select id,name as text,assign_to,DATE_FORMAT(begin_time,'%d-%m-%Y') as start_date,DATE_FORMAT(end_time,'%d-%m-%Y') as end_date, id_num as sort, pid as parent from tw_task where project_id = #{projectId} order by sort")
    List<Map> selectTaskGanttByProjectId(@Param("projectId") String  projectId);
}
