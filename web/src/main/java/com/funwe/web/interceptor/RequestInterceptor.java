package com.funwe.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.funwe.common.utils.JwtUtil;
import com.funwe.core.ds.DynamicDataSourceContextHolder;
import com.funwe.core.model.CurrentUser;
import com.funwe.core.model.JsonResult;
import com.funwe.dao.redis.SystemInfoRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * @author Administrator
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Autowired
    private SystemInfoRedis systemInfoRedis;

    /**
     * 进入controller层之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //System.out.println(request.getServletPath());
        //获取请求头的token, 没有的化返回失败，前端需要登录
        String token = request.getHeader("token");

        if (token == null || "".equals(token)){
            returnJson(response, JSON.toJSONString(JsonResult.fail()));
            return false;
        }
        //校验token，过期等原因失败则前端需要重新登录
        String userName = JwtUtil.verityAndGetUser(token);
        if ("".equals(userName)) {
            returnJson(response, JSON.toJSONString(JsonResult.fail()));
            return false;
        }
        //判断权限，失败的话返回警告
        String url = request.getServletPath();

        List<String> needRoleList = systemInfoRedis.getRoleListByPermCode(url);
        List<String> userRoleList = systemInfoRedis.getRoleListByUserName(userName);
        for (String needRole : needRoleList){
            if (!userRoleList.contains(needRole)){
                returnJson(response, JSON.toJSONString(JsonResult.warn()));
                return false;
            }
        }

        //设置用户数据源
        String dataSourceId = "";
        if (userName != null)
        {
            dataSourceId = systemInfoRedis.getDataSourceByUserName(userName);
            if (dataSourceId != null && !dataSourceId.isEmpty()){
                DynamicDataSourceContextHolder.removeDataSourceRouterKey();
                DynamicDataSourceContextHolder.setDataSourceRouterKey(dataSourceId);
            }
        }

        //将用户信息对象通过setAttribute放入Request中
        CurrentUser currentUser = new CurrentUser();
        currentUser.setToken(token);
        currentUser.setUserName(userName);
        currentUser.setDataSourceId(dataSourceId);
        currentUser.setRoles(userRoleList);
        request.setAttribute("currentUser", currentUser);

        return true;
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            log.error("拦截器返回JSON失败", e);
        } finally {
            if (writer != null){
                writer.close();
            }
        }
    }


    /**
     * 处理请求完成后视图渲染之前的处理操作
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        DynamicDataSourceContextHolder.removeDataSourceRouterKey();
    }

    /**
     * 视图渲染之后的操作
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

}
