package org.jeecg.modules.estar.nd.service;

import org.jeecg.modules.estar.nd.entity.NdRecoveryfile;
import org.jeecg.modules.estar.nd.vo.RecoveryFileListVo;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: nd_recoveryfile
 * @Author: nbacheng
 * @Date:   2023-04-08
 * @Version: V1.0
 */
public interface INdRecoveryfileService extends IService<NdRecoveryfile> {
	 void deleteUserFileByDeleteBatchNum(String deleteBatchNum);

	List<RecoveryFileListVo> selectRecoveryFileList(String username);

	void restorefile(String deleteBatchNum, String filePath, String username);
}
