package com.carbon.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carbon.system.entity.CarbonArticle;
import com.carbon.system.param.CarbonArticleQueryParam;
import com.carbon.system.vo.CarbonArticleQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 碳文章 Mapper 接口
 * </p>
 *
 * @author Li Jun
 * @since 2021-08-01
 */
@Repository
public interface CarbonArticleMapper extends BaseMapper<CarbonArticle> {

}
