package org.jeecg.modules.estar.tw.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.jeecg.modules.estar.tw.entity.TwFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Description: 项目文件表
 * @Author: nbacheng
 * @Date:   2023-07-11
 * @Version: V1.0
 */
public interface TwFileMapper extends BaseMapper<TwFile> {
	 @Select("SELECT id,path_name,title,extension,file_size,object_type,organization_id,task_id,project_id,create_by,create_time,downloads,extra,deleted,file_url,file_type,deleted_time FROM tw_file WHERE id = #{fileId}")
	    Map selectFileById(@Param("fileId") String fileId);

	    @Select("SELECT * FROM tw_file WHERE project_id = #{params.projectId} AND deleted = #{params.deleted} ORDER BY id DESC")
	    IPage<Map> selectFileByProjectIdAndDelete(IPage<Map> page, @Param("params") Map params);
}
