package hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;



class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        //ThreadA : A사용자가 10000원을 주문
        int userAPrice = statefulService1.order("userA", 10000);

        //ThreadB : B사용자가 20000원을 주문
        int userBPrice = statefulService2.order("userB", 20000);  //이떄 기존 10000만언으로 셋팅된 객체가 20000만원으로 바낀다!!!

        //ThreadA : A사용자가 주문 금액 조회
//        System.out.println("statefulService1 = " + statefulService1);
        System.out.println("userAPrice = " + userAPrice);
        //assertThat(userAPrice).isEqualTo(20000);
    }




    static class TestConfig{
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }




}
