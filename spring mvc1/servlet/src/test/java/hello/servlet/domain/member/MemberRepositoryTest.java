package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;




class MemberRepositoryTest {  //test 클래스에는 public을 적어주지 않아도 됩니다.

    //테스트 대상 불러오기 (memberRepository)
    //MemberRepository memberRepository = new MemberRepository(); 는 싱글톤이라 사용하지 못한다.
    MemberRepository memberRepository = MemberRepository.getInstance();



    //테스트 1개가 끝날때마다 이후 동작  (테스트 순서는 보장이 안됨.)
    @AfterEach
    void afterEach() {
        memberRepository.clearStore();  //(map) 저장소 초기화
    }



    //회원 저장 + 회원 조회 (By Id)
    @Test
    void save() {
        //회원 객체 DTO 생성 + 회원정보 삽입
        Member member = new Member("hello", 20);

        //저장소에 회원정보 저장
        Member savedMember = memberRepository.save(member);

        //ID로 회원정보 조회
        Member findMember = memberRepository.findById(savedMember.getId());
        assertThat(findMember).isEqualTo(savedMember);  //savedMember 객체와 findMember 객체 비교
    }




    //회원 저장 + 모든 회원 조회
    @Test
    void findAll() {

        //회원 객체 DTO 생성 + 2개의 회원정보 삽입
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 30);

        //저장소에 회원정보 저장
        memberRepository.save(member1);
        memberRepository.save(member2);

        //response DTO result 객체를 리스트로 생성 + 모든 회원정보 삽입
        List<Member> result = memberRepository.findAll();


        //저장된 회원정보의 크기 반환 후 비교
        assertThat(result.size()).isEqualTo(2);        //회원 객체 크기가 2개가 맞는지 확인
        assertThat(result).contains(member1, member2);  //result객체에 member1과 member2가 있는지 확인

    }







}