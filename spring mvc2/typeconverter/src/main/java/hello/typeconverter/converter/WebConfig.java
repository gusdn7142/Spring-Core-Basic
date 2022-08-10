package hello.typeconverter.converter;

import hello.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;




@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addFormatters(FormatterRegistry registry) {

        //스프링 내부의 ConversionService에 컨버터 등록
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());

        //컨버터 등록
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        //포맷터 등록 (포맷터가 컨버터보다 우선순위가 높음)
        registry.addFormatter(new MyNumberFormatter());


    }






}
