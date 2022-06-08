package hello.servlet.web.frontcontroller.v3.controller;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {

        //요청으로 받은 파라미터 값 ("이름", "나이") 조회
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        //회원객체 생성 & 파라미터 값 삽입 & 회원 저장소에 저장
        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model에 회원 정보 저장
        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);  //response 모델
        return mv;
    }



}
