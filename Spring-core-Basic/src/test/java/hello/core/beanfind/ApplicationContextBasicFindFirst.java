package hello.core.beanfind;


import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


public class ApplicationContextBasicFindFirst {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    /* 성공 테스트 */
    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        //System.out.println("memberService " + memberService );
        //System.out.println("memberService.getClass()" + memberService.getClass());
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
        System.out.println(memberService);
    }




    @Test
    @DisplayName("빈을 타입으로만 조회")
    void findBeanByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
        System.out.println(memberService);
    }


    @Test
    @DisplayName("구현체 타입으로 빈 조회")   //구현체 타입으로 조회하는것은 이상적인 코드가 아니다. 유연성이 떨어진다.
    void findBeanByName2() {
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
        System.out.println(memberService);
    }


    /* 실패 테스트 (예외가 터져야 성공이다!!) */
    @Test
    @DisplayName("빈 이름으로 조회X")
    void findBeanByImplements() {
        //MemberService xxxxx = ac.getBean("xxxxx", MemberService.class);
        assertThrows(NoSuchBeanDefinitionException.class,      //이 로직 실행시 예외가 터져야 테스트가 성공한 것이다.
                () -> ac.getBean("xxxxx", MemberService.class ));
    }








}













