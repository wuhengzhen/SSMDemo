package com.zhen.base.service;

import com.zhen.base.dao.system.UserMapper;
import com.zhen.base.domain.system.User;
import com.zhen.common.master.BaseRequest;
import com.zhen.datasource.DataSourceConstant;
import com.zhen.datasource.DataSourceContextHolder;
import com.zhen.exception.BusinessException;
import com.zhen.util.RedisUtil;
import com.zhen.util.shiro.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : wuhengzhen
 * @Description :
 * @date : 2018/08/02 19:00
 * @copyright:
 */
@Component
public class LoginServiceImpl implements ILoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     *
     * @param loginId
     * @param password
     * @return
     */
    @Override
    public int login(String loginId, String password) {
        return 0;
    }

    /**
     * description :根据用户名查询用户信息
     * author : wuhengzhen
     * date : 2018-12-4 10:24
     *
     * @param baseRequest 公共参数
     */
    @Override
    public User selectUserByUsername(BaseRequest baseRequest) {
        // 切换数据库
        DataSourceContextHolder.setDataSourceType(DataSourceConstant.MYSQL);
        // 获取参数
        String userName = baseRequest.getValueFormData("userName");
        User user = userMapper.selectUserByUsername(userName);
        if (user != null) {
            logger.info("登录用户：{} 用户信息查询成功！", userName);
        } else {
            logger.error("登录用户：{} 用户信息查询为空！", userName);
            throw new BusinessException("用户信息查询为空！");
        }
        return user;
    }

    /**
     * description : 将用户信息缓存到Redis
     * author : wuhengzhen
     * date : 2018-12-5 15:27
     *
     * @param baseRequest
     */
    @Override
    public void saveUserInfoToRedis(BaseRequest baseRequest) {
        ShiroUser shiroUser = baseRequest.getValueFormData("shiroUser");
        // 将用户信息缓存到redis，并设置失效时间为：30分钟 1800ms
        boolean flag = RedisUtil.set(shiroUser.getSessionId(), shiroUser, 1800);
        if (flag) {
            logger.info("登录用户：{} - 将登陆信息放入Redis成功！", shiroUser.getUserCde());
        } else {
            logger.info("登录用户：{} - 将登陆信息放入Redis失败！", shiroUser.getUserCde());
            throw new BusinessException("redis data exception！");
        }
    }
}
