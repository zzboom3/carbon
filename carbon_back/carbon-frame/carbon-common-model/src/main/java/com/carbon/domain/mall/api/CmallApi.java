package com.carbon.domain.mall.api;

import com.carbon.domain.common.ApiResult;
import com.carbon.domain.mall.api.hystrix.CmallApiFallback;
import com.carbon.domain.system.vo.UmsMemberSetmealQueryVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * 系统服务Api
 * @author Li Jun
 */
@FeignClient(value = "carbon-cmall", fallbackFactory = CmallApiFallback.class)
@RequestMapping("cmall")
public interface CmallApi {

	@PostMapping("/umsMemberSetmeal/info")
	ApiResult<UmsMemberSetmealQueryVo> getMemberSetmeal(@RequestParam("id") int id);

	@PostMapping("/umsMember/openVip")
	ApiResult openVip(@RequestParam("setMealId") int setMealId) throws Exception;
}
