package org.jeecg.modules.estar.tw.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import org.jeecg.modules.estar.tw.entity.TwProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Description: 项目表
 * @Author: nbacheng
 * @Date:   2023-05-27
 * @Version: V1.0
 */

public interface TwProjectMapper extends BaseMapper<TwProject> {
	@Select("SELECT * FROM tw_project WHERE organization_id = #{id} ")
    List<TwProject> selectProjectByOrgId(@Param("id") String id);
	
	//删除到回收站
    @Update("UPDATE tw_project SET deleted = #{deleted} , deleted_time = #{deletedTime} WHERE id = #{id}")
    int updateRecycle(@Param("id") String id,@Param("deleted") Integer deleted,@Param("deletedTime") Date deletedTime);
    
    //更新归档标识
    @Update("UPDATE tw_project SET archive = #{archive} , archive_time = #{archiveTime} WHERE id = #{id}")
    int updateArctive(@Param("id") String id,@Param("archive") Integer archive,@Param("archiveTime") Date archiveTime);
    
    //选择收藏的项目
    @Select("SELECT a.* FROM tw_project a LEFT JOIN tw_project_collect b ON a.id=b.project_id WHERE a.deleted=0 AND b.user_id = #{userId}")
    List<TwProject> selectCollectProject(@Param("userId") String userId);
    
    @SuppressWarnings("rawtypes")
	@Select({"<script>",
        "SELECT p.id,p.cover,p.name,p.id,p.description,p.access_control_type,p.white_list,p.sort,p.deleted,p.template_id,p.schedule,p.create_time,p.organization_id,p.deleted_time,p.privated,p.prefix,p.open_prefix,p.archive,p.archive_time,p.open_begin_time,p.open_task_private,p.task_board_theme,p.begin_time,p.end_time,p.auto_update_schedule,",
        "pm.user_id FROM tw_project AS p JOIN tw_project_member AS pm ON p.id = pm.project_id LEFT JOIN tw_project_collect AS pc ON p.id = pc.project_id  ",
        " WHERE ",
        " pm.user_id = #{params.memberId} AND p.organization_id = #{params.organizationId} " ,
        "<if test='params.deleted!=-1 '>",
        "AND p.deleted <![CDATA[ = ]]> #{params.deleted}",
        "</if>",
        "<if test='params.archive!=-1 '>",
        "AND p.archive <![CDATA[ = ]]> #{params.archive}",
        "</if>",
        " ORDER BY pc.id DESC,p.id DESC",
        "</script>"})
    IPage<Map> selectMemberProjects(IPage<Map> page,@Param("params") Map params);
    
    @Select("SELECT id,cover,name,description,access_control_type,white_list, sort,deleted,template_id,schedule,create_time,organization_id,deleted_time,privated,prefix,open_prefix,archive,archive_time,open_begin_time,open_task_private,task_board_theme,begin_time,end_time,auto_update_schedule FROM tw_project WHERE id = #{id}")
    Map selectProjectById(@Param("id") String id);
    
    @Select("select tl.ope_type as type,tl.action_type as action_type,tl.source_id as source_id,tl.remark as remark,tl.content as content,tl.is_comment as is_comment,tl.create_time as create_time,p.name as project_name,p.id as project_id,m.avatar as member_avatar,m.realname as member_name from tw_project_log as tl join tw_project as p on tl.project_id = p.id join sys_user as m on tl.member_id = m.username where p.id = #{projectId} and p.deleted = 0 order by tl.id desc ")
    IPage<Map> selectProjectLogByProjectId(IPage<Map> page,@Param("projectId") String projectId);
    
    IPage<Map> selectTaskLogByProjectId(IPage<Map> page,@Param("list") List list);
    List<String> selectProjectIdsByMemberAndOrg(@Param("params") Map params);
}
