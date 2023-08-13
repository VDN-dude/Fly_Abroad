package com.example.fly_abroad.handler;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@Component
public class CustomHandlerInterceptor implements HandlerInterceptor {

    private final Logger log = Logger.getLogger(CustomHandlerInterceptor.class.getName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        int status = response.getStatus();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        switch (status) {
            case 404 -> {
                modelAndView.setViewName("/error/404");
                modelAndView.addObject("message", "Page or query element not found!");
                if (authentication != null) {
                    log.info("User '" + authentication.getName() + "' tried follow the link the URL: " + request.getRequestURI() + "?" + request.getQueryString());
                }
            }
            case 403 -> {
                modelAndView.setViewName("/error/403");
                modelAndView.addObject("message", "You have not access to this URL!");
                if (authentication != null) {
                    log.info("User '" + authentication.getName() + "' attempted to access the URL: " + request.getRequestURI());
                }
            }
            case 500 -> {
                modelAndView.setViewName("/error/500");
                modelAndView.addObject("message", "Sorry, but something went wrong on our side, our employees working on it.");
                log.warning("URL: " + request.getRequestURI() + ", throw " + " 500 error code");
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
