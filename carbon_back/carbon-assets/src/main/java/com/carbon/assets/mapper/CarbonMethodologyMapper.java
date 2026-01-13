package com.carbon.assets.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carbon.assets.entity.CarbonMethodology;
import com.carbon.assets.param.CarbonMethodologyQueryParam;
import com.carbon.assets.vo.CarbonMethodologyQueryVo;
import com.carbon.assets.vo.CarbonMethodologySelectVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 碳减排方法学 Mapper 接口
 * </p>
 *
 * @author Li Jun
 * @since 2021-07-31
 */
@Repository
public interface CarbonMethodologyMapper extends BaseMapper<CarbonMethodology> {

}
