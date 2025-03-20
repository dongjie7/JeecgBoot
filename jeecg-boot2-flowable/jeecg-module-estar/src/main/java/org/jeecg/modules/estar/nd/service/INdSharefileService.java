package org.jeecg.modules.estar.nd.service;

import org.jeecg.modules.estar.nd.entity.NdSharefile;
import org.jeecg.modules.estar.nd.vo.ShareFileListVO;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 网盘分享文件表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
public interface INdSharefileService extends IService<NdSharefile> {

	List<ShareFileListVO> selectShareFileList(String shareBatchNum, String filePath);

}
