package com.shf.sg02springbootstatic.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 判断方法参数使用用当前的参数解析器进行解析
     * @param parameter the method parameter to check
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
//        如果方法参数有被CurrentUserId注解，就能把我们的解析器解析
        return parameter.hasParameterAnnotation(CurrentUserId.class);
    }

    /**
     * 进行参数解析方法，可以在方法中获取对应的数据，然后把数据作为返回值返回。方法的返回值就会赋值给对应的方法
     * @param parameter the method parameter to resolve. This parameter must
     * have previously been passed to {@link #supportsParameter} which must
     * have returned {@code true}.
     * @param mavContainer the ModelAndViewContainer for the current request
     * @param webRequest the current request
     * @param binderFactory a factory for creating {@link WebDataBinder} instances
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        获取请求头中的token
        String token = webRequest.getHeader("token");
        if (StringUtils.hasText(token)) {
//            解析token，获取userId
            return "111";
        }
        return null;
    }
}
