package com.carbon.assets.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carbon.assets.entity.CarbonProject;
import com.carbon.assets.entity.CarbonProjectContent;
import com.carbon.assets.param.CarbonDataSubmissionQueryParam;
import com.carbon.assets.param.CarbonProjectQueryParam;
import com.carbon.assets.vo.CarbonDetectionDataVo;
import com.carbon.assets.vo.CarbonProjectListVo;
import com.carbon.assets.vo.CarbonProjectQueryVo;
import com.carbon.common.api.Paging;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 碳减排项目 Mapper 接口
 * </p>
 *
 * @author Li Jun
 * @since 2021-07-31
 */
@Mapper
public interface CarbonProjectMapper extends BaseMapper<CarbonProject> {

}
