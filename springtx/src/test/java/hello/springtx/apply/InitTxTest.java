package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@SpringBootTest
public class InitTxTest {

    @Autowired
    Hello hello;   //Hello 객체에 의존성 주입


    //초기화 코드 테스트
    @Test
    void go() {
        //초기화 코드는 스프링이 초기화 시점에 호출한다.

        //hello.init2();
    }


    //Hello를 Bean으로 등록
    @TestConfiguration
    static class InitTxTestConfig {
        @Bean
        Hello hello() {
            return new Hello();
        }
    }


    @Slf4j
    static class Hello {

        @PostConstruct        //초기화 메서드
        @Transactional        //트랜잭션 적용... 초기화 코드가 먼저 호출되고 Transactional이 적용되기 때문에.. 초기화 시점에는 트랜잭션 획득 불가능..
        public void initV1() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();  //현재 쓰레드에 트랜잭션이 적용되어 있는지 확인할 수 있는 기능  (true or false 리턴)
            log.info("@PostConstruct로 트랜잭션 적용 확인 = {}", isActive);
        }

        @EventListener(value = ApplicationReadyEvent.class)   //스프링 컨테이너가 완전히 초기화를 끝내고, 실행 준비가 되었을 때 발생하는 이벤트
        @Transactional        //트랜잭션 적용... 초기화, AOP등의 과정이 모두 끝나고 EventListener가 실행되기 때문에  트랜잭션이 적용되어 있다.
        public void init2() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();  //현재 쓰레드에 트랜잭션이 적용되어 있는지 확인할 수 있는 기능  (true or false 리턴)
            log.info("@EventListener(ApplicationReadyEvent.class)로 트랜잭션 적용 확인 = {}", isActive);
        }




    }
}