package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CommonResult;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截器  拦截未登录的请求
 */
@WebFilter(filterName = "checkLoginFilter",urlPatterns = "/*")
public class CheckLoginFilter implements Filter {
    // 路径匹配器 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取本次请求的URI
        String requestURI = request.getRequestURI();

        // 定义不需要拦截的请求
        String[] urls = new String[]{
                "/employee/login",
//                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html/**",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"


        };

        // 检查如果请求路径满足不被拦截的路径 过滤器执行放行
        boolean check = check(urls, requestURI);
        if (check){
            filterChain.doFilter(request,response);
            return;
        }

        // 后台
        // 1.获取客户端携带回来的Session中的employee  如果不为空表示已经登录 过滤器执行放行
        Object employee = request.getSession().getAttribute("employee");
        if ( employee != null){
            Long operatorUserID = (Long)employee;
            // 向ThreadLocal中存入操作员工的员工的ID
            BaseContext.setCurrentId(operatorUserID);
            filterChain.doFilter(request,response);
            return;
        }

        // 移动端
        // 1.获取客户端携带回来的Session中的user  如果不为空表示已经登录 过滤器执行放行
        Object user = request.getSession().getAttribute("user");
        if (user != null){
            Long operatorUserID = (Long)user;
            // 向ThreadLocal中存入操作员工的员工的ID
            BaseContext.setCurrentId(operatorUserID);
            filterChain.doFilter(request,response);
            return;
        }

        // 如果未登录则返回未登录结果  并向页面写入未登录状态的JSON数据
        response.getWriter().write(JSON.toJSONString(CommonResult.error("NOTLOGIN")));
    }

    /**
     * 检查匹配路径
     * @param urls 不需要拦截的请求
     * @param requestURI 当前请求
     * @return 如果当前请求与不需要拦截的请求相匹配则返回true 否则返回false
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls){
            if (PATH_MATCHER.match(url,requestURI)){
                return true;
            }
        }
        return false;
    }
}
