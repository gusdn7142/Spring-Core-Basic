package hello.core.lifecycle;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean((NetworkClient.class));  //타입으로 Bean 조회
        ac.close();

    }


    @Configuration
    static class LifeCycleConfig{

        //@Bean(initMethod = "init", destroyMethod = "close")
        @Bean
        public NetworkClient networkClient(){
            NetworkClient networkCLient = new NetworkClient();  //NetworkClient 생성자 호출
            networkCLient.setUrl("http://hello-spring.dev");  //객체 생성후 set()으로 url을 넣어줌.
            return networkCLient;
        }


    }



}
