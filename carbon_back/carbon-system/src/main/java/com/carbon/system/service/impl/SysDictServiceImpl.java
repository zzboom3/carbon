package com.carbon.system.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.common.enums.ExpCodeEnum;
import com.carbon.common.exception.CommonBizException;
import com.carbon.domain.system.vo.SysDictModelVo;
import com.carbon.system.entity.SysDict;
import com.carbon.system.entity.SysDictItem;
import com.carbon.system.mapper.SysDictItemMapper;
import com.carbon.system.mapper.SysDictMapper;
import com.carbon.system.param.SysDictAddParam;
import com.carbon.system.param.SysDictItemAddParam;
import com.carbon.system.param.SysDictItemQueryParam;
import com.carbon.system.service.SysDictItemService;
import com.carbon.system.service.SysDictService;
import com.carbon.system.param.SysDictQueryParam;
import com.carbon.system.vo.SysDictItemQueryVo;
import com.carbon.system.vo.SysDictQueryVo;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.common.api.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 系统字典  服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDict> implements SysDictService {
    @Resource
    private SysDictMapper sysDictMapper;
    @Override
    public List<SysDictModelVo> getAllDict() {
        return sysDictMapper.getAllDict();
    }
}
