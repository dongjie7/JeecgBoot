package org.jeecg.modules.estar.nd.service;

import org.jeecg.modules.estar.nd.dto.ShareFileDTO;
import org.jeecg.modules.estar.nd.dto.ShareListDTO;
import org.jeecg.modules.estar.nd.entity.NdShare;
import org.jeecg.modules.estar.nd.vo.ShareFileVO;
import org.jeecg.modules.estar.nd.vo.ShareListVO;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 网盘分享表
 * @Author: nbacheng
 * @Date:   2023-04-06
 * @Version: V1.0
 */
public interface INdShareService extends IService<NdShare> {

	List<ShareListVO> selectShareList(ShareListDTO shareListDTO, String username);

	ShareFileVO shareFile(ShareFileDTO shareSecretDTO);

}
