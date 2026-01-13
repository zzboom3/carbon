package com.carbon.system.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carbon.common.exception.CommonBizException;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.system.entity.DataPanel;
import com.carbon.system.entity.SysAccount;
import com.carbon.system.entity.SysTenant;
import com.carbon.system.service.*;
import com.carbon.system.vo.*;
import com.carbon.system.mapper.DataPanelMapper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 数据面板 服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2021-07-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPanelServiceImpl extends BaseServiceImpl<DataPanelMapper, DataPanel> implements DataPanelService {

}
