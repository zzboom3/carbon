package com.carbon.assets.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.assets.entity.CarbonProjectDoc;
import com.carbon.assets.mapper.CarbonProjectDocMapper;
import com.carbon.assets.service.CarbonProjectDocService;
import com.carbon.assets.param.CarbonProjectDocQueryParam;
import com.carbon.assets.vo.CarbonProjectDocQueryVo;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.common.api.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.io.Serializable;


/**
 * <p>
 * 碳减排项目文档 服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2022-04-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CarbonProjectDocServiceImpl extends BaseServiceImpl<CarbonProjectDocMapper, CarbonProjectDoc> implements CarbonProjectDocService {

}
