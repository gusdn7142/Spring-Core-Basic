package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    BasicService basicService;

    //테스트
    @Test
    void proxyCheck() {
        //BasicService$$EnhancerBySpringCGLIB...
        log.info("Aop 클래스={}", basicService.getClass());

        //basicService에 스프링 AOP 프록시가 적용되었는지 확인
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();

    }


    //테스트
    @Test
    void txTest() {
        basicService.tx();      //트랜잭션 적용 테스트
        basicService.nonTx();   //트랜잭션 미적용 테스트
    }




    //basicService를 Bean으로 등록
    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }


    @Slf4j
    static class BasicService {

        //트랜잭션 적용
        @Transactional
        public void tx() {
            log.info("트랜잭션 적용");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();    //트랜잭션 적용여부 조회 (true or false)
            log.info("트랜잭션 active={}", txActive);
        }

        //트랜잭션 미적용
        public void nonTx() {
            log.info("트랜잭션 미적용");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();    //트랜잭션 적용여부 조회 (true or false)
            log.info("트랜잭션 active={}", txActive);
        }
    }


}