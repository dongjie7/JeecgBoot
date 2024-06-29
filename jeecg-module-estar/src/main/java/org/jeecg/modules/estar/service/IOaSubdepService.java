package org.jeecg.modules.estar.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.estar.entity.OaSubdep;

/**
 * @Description: oa_subdep
 * @Author: nbacheng
 * @Date:   2022-02-24
 * @Version: V1.0
 */
public interface IOaSubdepService extends IService<OaSubdep> {
	public List<OaSubdep> getSubDep(String upperno);
	public List<OaSubdep> getDep(String depno);
}
