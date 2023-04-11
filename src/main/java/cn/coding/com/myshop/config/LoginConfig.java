package cn.coding.com.myshop.config;

import cn.coding.com.myshop.interceptor.AdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new AdminInterceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/login", "/api/**", "/css/**", "/images/**", "/js/**",
                "/data_for_datatable/**", "/bike/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/home");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        WebMvcConfigurer.super.configureViewResolvers(registry);
    }
}
