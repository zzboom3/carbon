package com.carbon.assets.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.assets.entity.CarbonMethodology;
import com.carbon.assets.mapper.CarbonMethodologyMapper;
import com.carbon.domain.assets.vo.MethodologyUploadParam;
import com.carbon.assets.service.CarbonMethodologyService;
import com.carbon.assets.param.CarbonMethodologyQueryParam;
import com.carbon.assets.vo.CarbonMethodologyQueryVo;
import com.carbon.assets.vo.CarbonMethodologySelectVo;
import com.carbon.common.service.BaseServiceImpl;
import com.carbon.common.api.Paging;
import com.carbon.domain.common.constant.RocketDelayLevelConstant;
import com.carbon.domain.common.constant.RocketMqName;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


/**
 * <p>
 * 碳减排方法学 服务实现类
 * </p>
 *
 * @author Li Jun
 * @since 2021-07-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CarbonMethodologyServiceImpl extends BaseServiceImpl<CarbonMethodologyMapper, CarbonMethodology> implements CarbonMethodologyService {

}
