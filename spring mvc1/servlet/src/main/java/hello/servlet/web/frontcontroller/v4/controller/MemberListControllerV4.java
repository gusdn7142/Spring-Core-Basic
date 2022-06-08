package hello.servlet.web.frontcontroller.v4.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.List;
import java.util.Map;




public class MemberListControllerV4 implements ControllerV4 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();


    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {

        //모든 회원 정보 조회
        List<Member> members = memberRepository.findAll();

        //Model에 회원 정보 저장 + 논리적인 뷰 이름 리턴
        model.put("members", members);    //model에 ~삽입
        return "members";                 //viewName 반환


    }




}
