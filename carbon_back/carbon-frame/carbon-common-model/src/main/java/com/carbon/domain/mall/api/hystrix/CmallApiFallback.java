package com.carbon.domain.mall.api.hystrix;

import com.carbon.domain.common.ApiResult;
import com.carbon.domain.mall.api.CmallApi;
import com.carbon.domain.system.vo.UmsMemberSetmealQueryVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CmallApiFallback implements FallbackFactory<CmallApi> {
    @Override
    public CmallApi create(Throwable cause) {
        ApiResult result = ApiResult.fail("商城服务不可用");
        return new CmallApi() {
            @Override
            public ApiResult<UmsMemberSetmealQueryVo> getMemberSetmeal(int id) {
                return result;
            }

            @Override
            public ApiResult openVip(int setMealId) {
                return result;
            }
        };
    }
}
