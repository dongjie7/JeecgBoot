package org.jeecg.modules.estar.tw.mapper;

import org.jeecg.modules.estar.tw.entity.TwTaskStages;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Description: 任务列表
 * @Author: nbacheng
 * @Date:   2023-05-29
 * @Version: V1.0
 */
public interface TwTaskStagesMapper extends BaseMapper<TwTaskStages> {

	@Select("SELECT * FROM tw_task_stages a  WHERE a.project_id = #{projectId}")
    List<Map> selectTaskStageByProjectId(@Param("projectId")  String projectId);
	
	@Select("SELECT max(sort) as sort FROM tw_task_stages a  WHERE a.project_id = #{projectId}")
    Integer selectMaxSortByProjectId(@Param("projectId")  String projectId);
	
	@Select("SELECT * FROM tw_task_stages a  WHERE a.project_id = #{params.projectId} order by sort asc,id asc")
    IPage<TwTaskStages> selectTaskStageByProjectIdForPage(IPage page, @Param("params") Map params);
	
	//canRead
    @Select("SELECT * FROM tw_task_member WHERE task_id = #{taskId} AND member_id = #{memberId}")
    Map selectCanRead(@Param("taskId") String taskId,@Param("memberId") String memberId);
    @Select("select * from tw_task_like where task_id= #{taskId} and member_id = #{memberId}")
    Map selectTaskLike(@Param("taskId") String taskId,@Param("memberId") String memberId);
    @Select("select * from tw_collection a where a.coll_type='task' and a.source_id=#{taskId} and a.member_id=#{memberId}")
    Map selectTaskStared(@Param("taskId") String taskId,@Param("memberId") String memberId);
    //parentDone
    @Select("SELECT done,deleted FROM tw_task WHERE id = #{pId}")
    Map selectParentDone(@Param("pId") String pId);
    
    //ChildCount0
    @Select("SELECT COUNT(id) AS tp_count FROM tw_task WHERE pid = #{pId} AND deleted = 0")
    Map selectChildCount0(@Param("pId") String pId);
    //ChildCount1
    @Select("SELECT COUNT(id) AS tp_count FROM tw_task WHERE pid = #{pId} AND deleted = 0 AND done = 1")
    Map selectChildCount1(@Param("pId") String pId);
    
  //HasUnDone 是否有子任务未完成
    @Select("SELECT COUNT(id) AS tp_count FROM tw_task WHERE pid = #{pId} AND done = 0 AND deleted = 0")
    Map selectHasUnDone(@Param("pId") String pId);

    //HasComment
    @Select("SELECT COUNT(id) AS tp_count FROM tw_project_log WHERE source_id = #{id} AND ope_type = 'task' AND is_comment = 1")
    Map selectHasComment(@Param("id") String id);

    
    //HasSource
    @Select("SELECT COUNT(id) AS tp_count FROM tw_source_link WHERE link_id = #{id} AND link_type = 'task'")
    Map selectHasSource(@Param("id") String id);
    
    @Select("select count(1) from tw_task where project_id=#{projectId}")
    Integer selectCountByProjectId(@Param("projectId") String projectId);
    @Select("select count(1) from tw_task where project_id=#{projectId} and done = 1 ")
    Integer selectCountByProjectIdAndDone(@Param("projectId") String projectId);
    
    @Select("select tm.member_id from tw_task_member as tm join sys_user as m on tm.member_id = m.username where tm.task_id = #{taskId} and m.username = #{name} ")
    String selectMemberIdOne(@Param("taskId") String taskId,@Param("name") String name);
    
    @Select("select id,name as text,null assign_to,null as start_date,null as end_date,sort,null parent from tw_task_stages where project_id = #{projectId} order by sort" )
    List<Map> selectTaskStagesGanttByProjectId(@Param("projectId") String  projectId);
}
