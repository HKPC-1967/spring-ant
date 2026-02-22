package org.hkpc.dtd.component.postgres.dao;

import org.hkpc.dtd.component.postgres.model.UserRole;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRole row);

    int insertSelective(UserRole row);

    UserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole row);

    int updateByPrimaryKey(UserRole row);
}
