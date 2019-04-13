package com.kio.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kio.dao.BizSystemLogDao;
import com.kio.entity.SystemLog;

/**
 * 系统日志过滤器，通过过滤请求自动向数据库添加日志
 *
 * @author lqjlqj
 */
@WebFilter(filterName = "/SystemLogFilter", urlPatterns = "/*")
public class SystemLogFilter implements Filter {

    public SystemLogFilter() {

    }

    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String id = request.getSession().getId();
        String ip = request.getHeader("Host");

        String path = request.getServletPath();
        String operation;

        switch (path) {
            case "/createTask":
                operation = "创建任务#" +
                        "#" +
                        request.getParameter("iTaskType") +
                        "#" + response.getStatus();
                break;
            case "/executeTask":
                operation = "执行任务#" + request.getParameter("sTaskCode") +
                        "#" + request.getParameter("iTaskType") +
                        "#" + response.getStatus();
                break;
            case "/changeSystemParameters":
                operation = "改变系统参数#" + response.getStatus();
                break;
            case "/getSystemParameters":
                operation = "查询系统参数#" + response.getStatus();
                break;
            default:
                chain.doFilter(req, res);
                return;
        }


        SystemLog log = new SystemLog();
        log.setsCreateTime(new Date());
        log.setsOperation(operation);
        log.setsSession(id);
        if (ip.contains("localhost")||ip.contains("127.0.0.1"))
            log.setsIsSelf(1);
        else
            log.setsIsSelf(0);

        BizSystemLogDao.insertOperation(log);

        chain.doFilter(req, res);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) {

    }

}
