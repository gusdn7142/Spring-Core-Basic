package hello.springtx.apply;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired  //필드 주입
    CallService callService;


    //테스트1 : callService 프록시 객체 생성 확인
    @Test
    void printProxy() {
        log.info("callService class={}", callService.getClass());
    }

    //테스트2
    @Test
    void internalCall() {
        callService.internal();
    }

    //테스트3
    @Test
    void externalCall() {
        callService.external();
    }


    //CallService를 Bean으로 등록
    @TestConfiguration
    static class InternalCallV1Config {
        @Bean
        CallService callService() {
            return new CallService();
        }
    }


    @Slf4j
    static class CallService {

        public void external() {
            log.info("call external");
            printTxInfo();
            internal();
        }

        @Transactional   //트랜잭션 적용
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }


        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();        //트랜잭션 적용여부 조회 (true or false)
            log.info("트랜잭션 active={}", txActive);
        }
    }



}