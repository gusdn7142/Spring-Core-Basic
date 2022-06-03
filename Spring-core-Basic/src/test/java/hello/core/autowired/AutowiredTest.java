package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {


    @Test
    void AutowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);


    }


    static class TestBean {

        //@Autowired(required=false)
        @Autowired(required = false)
        public void setBean1(Member noBean1) {
            System.out.println("setBean1 = " + noBean1);
        }

        //org.springframework.lang.@Nullable
        @Autowired
        public void setBean2(@Nullable Member noBean2) {
            System.out.println("setBean2 = " + noBean2);
        }

        //Optional<>
        @Autowired
        public void setBean3(Optional<Member> noBean3) {
            System.out.println("setBean3 = " + noBean3);
        }
    }





}
