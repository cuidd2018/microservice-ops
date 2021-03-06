package com.yonyou.microservice.gate.admin.mapper;

import com.yonyou.microservice.gate.admin.entity.Menu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
/**
 * @author joy
 */
public interface MenuMapper extends Mapper<Menu> {
	/**
	 * 根据角色id和角色类型查询菜单
	 * @param authorityId，角色id
	 * @param authorityType，角色类型
	 * @return
	 */
    public List<Menu> selectMenuByAuthorityId(@Param("authorityId") String authorityId,@Param("authorityType") String authorityType);

    /**
     * 根据用户和组的权限关系查找用户可访问菜单
     * @param userId
     * @return
     */
    public List<Menu> selectAuthorityMenuByUserId (@Param("userId") int userId);

    /**
     * 根据用户和组的权限关系查找用户可访问的系统
     * @param userId
     * @return
     */
    public List<Menu> selectAuthoritySystemByUserId (@Param("userId") int userId);
}