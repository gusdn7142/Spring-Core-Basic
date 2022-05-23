package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        //basePackages = "hello.core.member",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = Configuration.class))







public class AutoAppConfig {

//    @Bean(name = "memoryMemberRepository")   //이미 자동으로 설정되어 있는 Bean을 이름이 같게 하나 더 생성
//    MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//    }


}
