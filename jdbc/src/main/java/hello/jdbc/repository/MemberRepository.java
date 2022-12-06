package hello.jdbc.repository;

import hello.jdbc.domain.Member;


public interface MemberRepository {

    //1. 회원 등록 함수
    Member save(Member member);

    //2. 회원 조회 (MemberId 활용)
    Member findById(String memberId);

    //3. 회원 정보 변경 함수
    void update(String memberId, int money);

    //4. 회원 정보 삭제 함수
    void delete(String memberId);

}
