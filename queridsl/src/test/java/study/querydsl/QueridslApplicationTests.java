package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.From;

@SpringBootTest
@Transactional   //트랜잭션 (+커밋) 설정
class QueridslApplicationTests {

	//@Autowired
	@PersistenceContext //Bean 등록
	EntityManager em;

	@Test
	void contextLoads() {
		Hello hello = new Hello();   //Hello 엔티티 객체 생성
		em.persist(hello);           //영속화

		JPAQueryFactory query = new JPAQueryFactory(em);  //JPAQueryFactory : JPAQuery를 생성해주는 factory클래스
		//QHello qHello = new QHello("h");
		QHello qHello = QHello.hello;   //QHello 객체 생성

		Hello result = query        //QHello 객체 조회
				.selectFrom(qHello)
				.fetchOne();  //한번 호출에 하나의 Row를 조회

		Assertions.assertThat(result).isEqualTo(hello);                  //Hello 객체와 QHello 객체 비교
		Assertions.assertThat(result.getId()).isEqualTo(hello.getId());  //Hello 객체의 id와 QHello 객체의 id 비교


	}

}
