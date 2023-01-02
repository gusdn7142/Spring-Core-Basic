package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class TxLevelTest {

    @Autowired
    LevelService service;


    //테스트
    @Test
    void orderTest() {
        service.write();   //LevelService의 write() 메서드 호출
        service.read();   //LevelService의 read() 메서드 호출
    }


    //LevelService를 Bean으로 등록
    @TestConfiguration
    static class TxApplyLevelConfig {
        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }


    @Slf4j
    @Transactional(readOnly = true)  //읽기 전용O
    static class LevelService {

        @Transactional(readOnly = false)  //읽기 전용X
        public void write() {
            log.info("call write()");
            printTxInfo();
        }

        public void read() {
            log.info("call read()");
            printTxInfo();
        }




        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();        //트랜잭션 적용여부 조회 (true or false)
            log.info("트랜잭션 active={}", txActive);

            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();        //트랜잭션의 readOnly 적용 여부 확인 (true or false)
            log.info("트랜잭션 readOnly={}", readOnly);
        }

    }

}