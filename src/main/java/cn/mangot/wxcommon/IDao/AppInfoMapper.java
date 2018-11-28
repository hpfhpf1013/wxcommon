package cn.mangot.wxcommon.IDao;

import cn.mangot.wxcommon.entity.AppInfo;

public interface AppInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppInfo record);

    int insertSelective(AppInfo record);

    AppInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppInfo record);

    int updateByPrimaryKey(AppInfo record);
    
    AppInfo selectByappId(String appid);
}