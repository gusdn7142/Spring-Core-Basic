package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import javax.sql.DataSource;


@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired
    PlatformTransactionManager txManager;  //PlatformTransactionManager 참조변수에 의존성 주입


    //DataSourceTransactionManager를 Bean으로 등록
    @TestConfiguration
    static class Config {
        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }


    //테스트1 - 트랜잭션 커밋
    @Test
    void commit() {
        log.info("트랜잭션 시작");
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());  //트랜잭션 연결+얻기

        log.info("트랜잭션 커밋 시작");
        txManager.commit(status);    //트랜잭션 커밋

        log.info("트랜잭션 커밋 완료");
    }

    //테스트2 - 트랜잭션 롤백
    @Test
    void rollback() {
        log.info("트랜잭션 시작");
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());  //트랜잭션 연결+얻기

        log.info("트랜잭션 롤백 시작");
        txManager.rollback(status);    //트랜잭션 롤백

        log.info("트랜잭션 롤백 완료");
    }


    //테스트3 - 트랜잭션1 시작하여 커밋 후 커넥션 반납, 트랜잭션 2 시작하여 커밋 후 커넥션  반납
    @Test
    void double_commit() {
        log.info("트랜잭션1 시작");

        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());  //트랜잭션1 연결+얻기

        log.info("트랜잭션1 커밋");
        txManager.commit(tx1);       //트랜잭션1 커밋

        log.info("트랜잭션2 시작");

        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());  //트랜잭션2 연결+얻기

        log.info("트랜잭션2 커밋");
        txManager.commit(tx2);       //트랜잭션2 커밋
    }



    //테스트4 - 트랜잭션1 시작하여 커밋 후 커넥션 반납, 트랜잭션 2 시작하여 롤백 후 커넥션 반납
    @Test
    void double_commit_rollback() {
        log.info("트랜잭션1 시작");

        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());  //트랜잭션1 연결+얻기

        log.info("트랜잭션1 커밋");
        txManager.commit(tx1);       //트랜잭션1 커밋

        log.info("트랜잭션2 시작");

        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());  //트랜잭션2 연결+얻기

        log.info("트랜잭션2 롤백");
        txManager.rollback(tx2);     //트랜잭션2 롤백
    }


    //테스트5 -  하나의 로직에서 외부/내부 트랜잭션 실행 및 커밋
    @Test
    void inner_commit() {

        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());     //외부(논리) 트랜잭션 연결+얻기
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());                        //처음 실행된 트랜잭션인지 검증 : true

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());     //내부(논리) 트랜잭션 연결+얻기 : 외부 트랜잭션과 같은 커넥션 사용.
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());                        //처음 실행된 트랜잭션인지 검증 : false

        log.info("내부 트랜잭션 커밋");
        txManager.commit(inner);       //내부(논리) 트랜잭션 커밋

        log.info("외부 트랜잭션 커밋");
        txManager.commit(outer);       //외부(논리) 트랜잭션 커밋

    }



    //테스트6 - 하나의 로직에서 내부 트랜잭션 커밋⋆외부 트랜잭션 롤백
    @Test
    void outer_rollback() {

        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());     //외부(논리) 트랜잭션 연결+얻기
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());                        //처음 실행된 트랜잭션인지 검증 : true

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());     //내부(논리) 트랜잭션 연결+얻기 : 외부 트랜잭션과 같은 커넥션 사용.
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());                        //처음 실행된 트랜잭션인지 검증 : false

        log.info("내부 트랜잭션 커밋");
        txManager.commit(inner);       //내부(논리) 트랜잭션 커밋

        log.info("외부 트랜잭션 롤백");
        txManager.rollback(outer);       //외부(논리) 트랜잭션 롤백

    }


    //테스트7 - 하나의 로직에서 내부 트랜잭션 롤백⋆외부 트랜잭션 커밋
    @Test
    void inner_rollback() {

        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());     //외부(논리) 트랜잭션 연결+얻기
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());                        //처음 실행된 트랜잭션인지 검증 : true

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());     //내부(논리) 트랜잭션 연결+얻기 : 외부 트랜잭션과 같은 커넥션 사용.
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());                        //처음 실행된 트랜잭션인지 검증 : false

        log.info("내부 트랜잭션 롤백");
        txManager.rollback(inner);       //내부(논리) 트랜잭션 롤백...  rollback-only 표시

        log.info("외부 트랜잭션 커밋");
        //txManager.commit(outer);       //외부(논리) 트랜잭션 커밋...  Global transaction is marked as rollback-only but transactional code requested commit
        assertThatThrownBy(() -> txManager.commit(outer)).isInstanceOf(UnexpectedRollbackException.class);   //UnexpectedRollbackException 발생 여부 확인

    }



}