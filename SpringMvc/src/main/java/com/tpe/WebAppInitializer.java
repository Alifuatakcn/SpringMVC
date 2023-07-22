package com.tpe;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//web.xml yerine bu classı kullanırız.
//dispatcher servletın tanımlanması,
// configuration classlarının yerini gösterme
//bu iki işlem için: AbstractAnnotationConfigDispatcherServletInitializer
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//Dispatcher: Servlet WebAppContext->Controller,Handler Mapping,View Resolver
//            Root WebAppContext->DB ye erişim:repositories,services

    @Override//data erişim(hibernate/jdbc) için gerekli config class
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
          RootContextConfig.class
        };
    }

    @Override//Controller,Handler Mapping,View Resolver(SpringMVC) ile ilgili config class
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                WebMvcConfig.class
        };
    }


    //http://localhost:8080/SpringMvc/....
    @Override//hangi url ile gelen istekleri servlet tarafından karşılanacak/
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}
