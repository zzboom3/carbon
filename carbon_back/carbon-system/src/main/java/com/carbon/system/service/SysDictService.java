package com.carbon.system.service;

import com.carbon.domain.system.vo.SysDictModelVo;
import com.carbon.system.entity.SysDict;
import com.carbon.common.service.BaseService;
import com.carbon.system.param.SysDictAddParam;
import com.carbon.system.param.SysDictItemAddParam;
import com.carbon.system.param.SysDictItemQueryParam;
import com.carbon.system.param.SysDictQueryParam;
import com.carbon.system.vo.SysDictItemQueryVo;
import com.carbon.system.vo.SysDictQueryVo;
import com.carbon.common.api.Paging;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统字典  服务类
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-16
 */
public interface SysDictService extends BaseService<SysDict> {
    /**
     * 获取所有字典
     * @return list
     */
    List<SysDictModelVo> getAllDict();
}
