package hello.core.beandefinition;


import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanDefinitionTest {


    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    //ApplicationContext를 하면 getBeanDefinition()가 없다!!

    @Test
    @DisplayName("빈 설정 메타정보 확인")

    void findApplicationBean() {

        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);  //Bean 정보 얻기

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {  //사용자가 정의한 bean만 출력
                System.out.println("beanDefinitionName = " + beanDefinitionName +
                        " beanDefinition = " + beanDefinition);
            }
        }
    }


}
