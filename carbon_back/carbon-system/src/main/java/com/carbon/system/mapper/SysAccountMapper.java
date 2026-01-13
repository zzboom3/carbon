package com.carbon.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carbon.system.entity.SysAccount;
import com.carbon.system.param.SysAccountQueryParam;
import com.carbon.system.vo.SysAccountQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 帐号  Mapper 接口
 * </p>
 *
 * @author Li Jun
 * @since 2021-06-11
 */
@Repository
public interface SysAccountMapper extends BaseMapper<SysAccount> {

}
