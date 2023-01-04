package hello.springtx.apply;


import lombok.RequiredArgsConstructor;
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
public class InternalCallV2Test {

    @Autowired  //필드 주입
    CallService callService;


    //테스트1 : callService 프록시 객체 생성 확인
    @Test
    void printProxy() {
        log.info("callService class={}", callService.getClass());
    }

//    //테스트2
//    @Test
//    void internalCall() {
//        callService.internal();
//    }

    //테스트3
    @Test
    void externalCall() {
        callService.external();
    }


    //CallService와 InternalService를 Bean으로 등록
    @TestConfiguration
    static class InternalCallV1Config {
        @Bean
        CallService callService() {
            return new CallService(internalService());
        }

        @Bean
        InternalService internalService(){
            return new InternalService();
        }
    }


    @Slf4j
    @RequiredArgsConstructor
    static class CallService {

        private final InternalService internalService;

        public void external() {
            log.info("call external");
            printTxInfo();
            internalService.internal();   //internalService의 internal() 메서드 호출
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();        //트랜잭션 적용여부 조회 (true or false)
            log.info("트랜잭션 active={}", txActive);
        }
    }



    static class InternalService{   // @Transactional이 포함되어 있으므로 프록시 객체로 Bean으로 등록됨

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