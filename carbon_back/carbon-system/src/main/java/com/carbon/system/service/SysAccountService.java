package com.carbon.system.service;

import com.carbon.domain.system.param.ChangePasswordParam;
import com.carbon.domain.system.param.SysAccountParam;
import com.carbon.system.entity.SysAccount;
import com.carbon.common.service.BaseService;
import com.carbon.system.param.*;
import com.carbon.system.vo.SysAccountQueryVo;
import com.carbon.common.api.Paging;
import com.carbon.domain.system.vo.SysAccountModelVo;
import java.util.List;

/**
 * <p>
 * 帐号  服务类
 * </p>
 *
 * @author Li Jun
 * @since 2021-06-11
 */
public interface SysAccountService extends BaseService<SysAccount> {

    boolean addSysAccount(SysAccountParam param);

    boolean updatePassword(ChangePasswordParam param);

    List<SysAccountModelVo> getAccountList();
}
