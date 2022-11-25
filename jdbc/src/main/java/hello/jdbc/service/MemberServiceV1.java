package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;



@RequiredArgsConstructor    //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;   //MemberRepositoryV1 생성자 주입

    //계좌이체 함수 : 2명의 회원간에 돈 거래
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        //시작
        Member fromMember = memberRepository.findById(fromId);  //보내는 이
        Member toMember = memberRepository.findById(toId);      //받는 이

        //계좌이체 로직 (+예외 발생 포함)
        memberRepository.update(fromId, fromMember.getMoney() - money);   //보내는이의 돈에서 money를 뺴준는 Update 쿼리 수행
        validation(toMember);                                                   //받는이의 memberId가 "ex"일떄 예외를 발생시켜 정상적인 계좌이체를 실패시킴
        memberRepository.update(toId, toMember.getMoney() + money);   //받는이의 돈에서 money를 더해주는 Update 쿼리 수행
        //커밋, 롤백
    }

    //memberID가 "ex"인 회원이 있으면 IllegalStateException 예외 발생
    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
