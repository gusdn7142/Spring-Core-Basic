package scope;


import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

public class PrototypeTest {

    @Test
    public void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);



        //@PreDestroy 수동 호출
//        prototypeBean1.destroy();
//        prototypeBean2.destroy();

        ac.close();   //종료될떄 소멸 콜백 메서드도 호출됨

    }


    @Scope("prototype")   //PrototypeBean.class를 환경설정 파일로 그대로 넣었기 때문에 @component 생략 가능
    static class PrototypeBean{
        @PostConstruct   //초기화 콜백 메서드
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy     //소멸 콜백 메서드
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }



}
