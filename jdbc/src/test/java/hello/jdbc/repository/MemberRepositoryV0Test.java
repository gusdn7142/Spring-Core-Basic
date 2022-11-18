package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();  //MemberRepositoryV0 객체 생성

    @Test
    void crud() throws SQLException {
        //Member 객체 생성 (회원 ID, 회원이 소지한 돈)
        Member member = new Member("memberV0", 10000);

        //DB에 회원 등록
        repository.save(member);                                           //DB에 member 정보 Insert
    }

}