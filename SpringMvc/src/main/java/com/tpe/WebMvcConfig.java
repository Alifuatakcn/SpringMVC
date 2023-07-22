package com.tpe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@ComponentScan("com.tpe")//default:com.tpe
@EnableWebMvc//WebMvc yi aktif et
public class WebMvcConfig implements WebMvcConfigurer {

    //view name(hi) e karşılık gelen jsp dosyasının çözümlenmesi:view resolver
    @Bean
    public InternalResourceViewResolver resolver(){
        InternalResourceViewResolver resolver=new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);//JavastandartTagLibrary:JSP dosyaları içinde HTML taglerini kullanarak
                                              //daha az kod yazmamızı sağlar.
        resolver.setPrefix("/WEB-INF/views/");//view dosyaları nerede(dizin)
        resolver.setSuffix(".jsp");//view dosyalarının uzantısı
        return resolver;
    }

    //css,image statik olan kaynakların dispatcher servleta gönderilmesine gerek yok
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").//bu pathdeki dosyaları statik olarak sun
                addResourceLocations("/resources/").
                setCachePeriod(0);//cacheleme için belirli bir periyod süresi verilebilir
    }
}
