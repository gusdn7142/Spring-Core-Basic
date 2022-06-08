package hello.servlet.web.frontcontroller.v4.controller;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {

        //요청으로 받은 파라미터 값 ("이름", "나이") 조회
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        //회원객체 생성 & 파라미터 값 삽입 & 회원 저장소에 저장
        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model에 회원 정보 저장 + 논리적인 뷰 이름 리턴
        model.put("member", member);  //model에 회원 객체
        return "save-result";         //viewName 반환




    }



}