package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.MemberSearchParam;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.repository.MemberJpaRepository;
import study.querydsl.repository.MemberRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchParam param) {
        return memberJpaRepository.search(param);   //param 조건을  where절 메서드에 넣어 Member 객체를 select한 결과를 MemberTeamDto객체로 반환
    }


    @GetMapping("/v2/members")
    public Page<MemberTeamDto> searchMemberV2(MemberSearchParam param, Pageable pageable) {

        return memberRepository.searchPageSimple(param, pageable);   //단순한 페이징, fetchResults() 사용 : 데이터 내용과 전체 카운트를 한번에 조회
    }

    @GetMapping("/v3/members")
    public Page<MemberTeamDto> searchMemberV3(MemberSearchParam param, Pageable pageable) {
        return memberRepository.searchPageComplex(param, pageable);  //복잡한 페이징 - fetch()와 fetchCount() 사용 : 데이터 내용과 전체 카운트를 별도로 조회
    }


}