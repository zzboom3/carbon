package com.carbon.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.api.Paging;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.system.entity.SysAccountRole;
import com.carbon.system.entity.SysRole;
import com.carbon.system.entity.SysRoleMenu;
import com.carbon.system.param.SysRoleMenuAddParam;
import com.carbon.system.param.SysRoleQueryParam;
import com.carbon.system.vo.SysAccountRoleVo;
import com.carbon.system.vo.SysRoleQueryVo;
import com.carbon.system.mapper.SysRoleMapper;
import com.carbon.system.mapper.SysRoleMenuMapper;
import com.carbon.system.service.SysAccountRoleService;
import com.carbon.system.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 角色  服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2021-06-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

}
