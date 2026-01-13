package com.carbon.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carbon.system.entity.SysMenu;
import com.carbon.system.param.SysMenuQueryParam;
import com.carbon.system.vo.SysMenuVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 菜单  Mapper 接口
 * </p>
 *
 * @author Li Jun
 * @since 2021-06-18
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}
