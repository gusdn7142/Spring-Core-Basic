package scope;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonTest {

    @Test
    public void singletonBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);

        System.out.println("singletonBean1 = " + singletonBean1);
        System.out.println("singletonBean2 = " + singletonBean2);

        assertThat(singletonBean1).isSameAs(singletonBean2);

        ac.close();   //종료될떄 소멸 콜백 메서드도 호출됨


    }




    @Scope("singleton")   //PrototypeBean.class를 환경설정 파일로 그대로 넣었기 때문에 @component 생략 가능
    static class SingletonBean{
        @PostConstruct  //초기화 콜백 메서드
        public void init() {
            System.out.println("SingletonBean.init");
        }

        @PreDestroy   //소멸 콜백 메서드
        public void destroy() {
            System.out.println("SingletonBean.destroy");
        }
    }





}
