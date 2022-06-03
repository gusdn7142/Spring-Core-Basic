package hello.core.beanfind;


import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextExtendsFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);



    @Test
    @DisplayName("부모 타입으로 조회시 자식이 둘 이상 있으면 중복 오류가 발생 ")
    void findBeanByParentTypeDuplicate() {
        //DiscountPolicy bean = ac.getBean(DiscountPolicy.class);
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(DiscountPolicy.class));  //타입으로만 Bean 객체 조회
    }


    @Test
    @DisplayName("부모 타입으로 조회시 자식이 둘 이상 있으면 빈 이름을 지정하면 정상 작동한다.")
    void findBeanByParentTypeBeanName() {
        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);  //Bean 이름과 Type으로 Bean 객체 조회
        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }


    @Test
    @DisplayName("특정 하위(구현체) 타입으로 조회")
    void findBeanBySubType() {
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);  //구현체 리턴 타입으로 조회
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }


    @Test
    @DisplayName("부모 타입에 해당되는 Bean들 조회 (이름, 객체)")
    void findAllBeanByParentType() {
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        assertThat(beansOfType.size()).isEqualTo(2);
        for(String key : beansOfType.keySet()){
            System.out.println("key = " + key + " value = " + beansOfType.get(key) );   //Bean 이름과 Bean 객체가 출력됨
        }
    }

    @Test
    @DisplayName("부모 타입으로 모든 Bean 조회 - Object")
    void findAllBeanByObjectType() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " value=" + beansOfType.get(key));
        }
    }






    @Configuration
    static class TestConfig {
        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }

        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }
    }






}











