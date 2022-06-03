package scope;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {


    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);  //싱글톤 Bean과 프로토타입 Bean

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);

        ac.close();

    }


//
//    static class ClientBean{
//        @Autowired
//        private Provider<PrototypeBean> prototypeBeanProvider;
//
//        public int logic() {
//            PrototypeBean prototypeBean = prototypeBeanProvider.get();
//            prototypeBean.addCount();
//            int count = prototypeBean.getCount();
//            return count;
//        }
//    }






//    @Test
//    public void prototypeFind() {
//        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
//
//        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
//        prototypeBean1.addCount();
//        assertThat(prototypeBean1.getCount()).isEqualTo(1);
//
//        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
//        prototypeBean2.addCount();
//        assertThat(prototypeBean2.getCount()).isEqualTo(1);
//
//
//        ac.close();   //종료될떄 소멸 콜백 메서드도 호출됨
//
//    }



    @Scope("singleton")
    static class ClientBean {

//        private final PrototypeBean prototypeBean;

        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;


//        @Autowired
//        ApplicationContext applicationContext;

//        //의존관계 주입
//        @Autowired
//        public ClientBean(PrototypeBean prototypeBean){   //프로토타입 Bean 생성 (+의존관계 주입)
//            this.prototypeBean = prototypeBean;
//        }


        //프로토타입 Bean의 Count 증가
        public int logic(){
//            PrototypeBean prototypeBean = applicationContext.getBean(PrototypeBean.class);

            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }



    }


    //프로토타입 Bean
    @Scope("prototype")
    static class PrototypeBean{
        private int count = 0 ;

        public void addCount() {
            count ++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init" + this);
        }

        @PreDestroy
        public void destroy() {   //호출 안될거임. 어챂피..
            System.out.println("PrototypeBean.destroy");
        }

    }







}
