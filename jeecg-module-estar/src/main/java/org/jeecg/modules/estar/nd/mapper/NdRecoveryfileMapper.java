package org.jeecg.modules.estar.nd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.estar.nd.entity.NdRecoveryfile;
import org.jeecg.modules.estar.nd.vo.RecoveryFileListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: nd_recoveryfile
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
public interface NdRecoveryfileMapper extends BaseMapper<NdRecoveryfile> {

	List<RecoveryFileListVo> selectRecoveryFileList(String username);

}
