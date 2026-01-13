package com.carbon.assets.service;

import com.carbon.assets.entity.CarbonProject;
import com.carbon.assets.entity.CarbonProjectContent;
import com.carbon.assets.param.CarbonDataSubmissionQueryParam;
import com.carbon.assets.param.CarbonProjectAddParam;
import com.carbon.assets.param.CarbonProjectOwnerDataParam;
import com.carbon.assets.vo.CarbonDetectionDataVo;
import com.carbon.assets.vo.CarbonProjectListVo;
import com.carbon.common.service.BaseService;
import com.carbon.assets.param.CarbonProjectQueryParam;
import com.carbon.assets.vo.CarbonProjectQueryVo;
import com.carbon.common.api.Paging;
import com.carbon.domain.common.ApiResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 碳减排项目 服务类
 * </p>
 *
 * @author Li Jun
 * @since 2021-07-31
 */
public interface CarbonProjectService extends BaseService<CarbonProject> {

}
