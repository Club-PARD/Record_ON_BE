package com.pard.record_on_be.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //수동으로 스프링 컨테이너에 Bean 등록하는 방법
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(
            ViewResolverRegistry registry
    )
    {
        MustacheViewResolver resolver =
                new MustacheViewResolver();
        resolver.setCharset("UTF-8");
        resolver.setContentType("text/html;charset=UTF-8");
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        registry.viewResolver(resolver);
    }
}