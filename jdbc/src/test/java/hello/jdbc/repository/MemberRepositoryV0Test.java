package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();  //MemberRepositoryV0 객체 생성

    @Test
    void crud() throws SQLException {
        //Member 객체 생성 (회원 ID, 회원이 소지한 돈)
        Member member = new Member("memberV0", 10000);

        //DB에 회원 등록
        repository.save(member);                                           //DB에 member 정보 Insert

        //DB에서 특정 회원 조회
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);   //isEqualTo() : findMember.equals(member)를 비교....  결과가 참인 이유는 롬복의 @Data는 해당 객체의 모든 필드를 사용하도록 equals()를 오버라이딩 하기 때문


        //DB에서 특정 회원 정보 변경 :  money를 10000 -> 20000으로 변경
        repository.update(member.getMemberId(), 20000);   //DB에서 member 정보 Update

        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);   //isEqualTo() : updatedMember.equals(member)를 비교..


        //DB에서 특정 회원 정보 삭제
        repository.delete(member.getMemberId());   //DB에서 member 정보 Delete

        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);   //NoSuchElementException 예외가 발생하는지 확인하여 검증



    }





}