package hello.servlet.web.frontcontroller.v3.controller;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.List;
import java.util.Map;

public class MemberListControllerV3 implements ControllerV3 {


    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {

        //모든 회원 정보 조회
        List<Member> members = memberRepository.findAll();

        //Model에 모든 회원 정보 저장
        ModelView mv = new ModelView("members");

        mv.getModel().put("members", members);

        //뷰의 논리 이름과 model 객체 리턴
        return mv;
    }


}
