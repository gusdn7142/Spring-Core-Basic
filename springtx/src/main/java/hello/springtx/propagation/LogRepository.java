package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;


@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepository {

    //@PersistenceContext                   //생략 가능
    private final EntityManager em;      //EntityManager에 의존성 주입

    //로그 등록
    @Transactional  //추가
    public void save(Log logMessage) {
        log.info("log 저장");
        em.persist(logMessage);  //영속성 컨텍스트에의 1차 캐시에 member 객체를 저장하고 쓰기지연 SQL 저장소에 Insert 쿼리 저장 후 커밋될떄 DB에 전달

        if (logMessage.getMessage().contains("로그예외")) {  //LOg의 message가 "로그예외"이면 예외 발생
            log.info("log 저장시 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    //(message를 통해)로그 조회
    public Optional<Log> find(String message) {
        return em.createQuery("select l from Log l where l.message = :message", Log.class)
                .setParameter("message", message)
                .getResultList().stream().findAny();    //결과가 2개이면 먼저 조회된 것을 반환
    }

}