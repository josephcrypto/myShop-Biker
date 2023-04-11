package cn.coding.com.myshop.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String username = (String) request.getSession().getAttribute("username");
            if (username == null || username.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            } else if ( username.equals("admin")) {
                return true;
            }
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("Execution the postHandle method of Interceptor . . . .");
    }
}
