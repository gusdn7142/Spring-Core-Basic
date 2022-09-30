package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchParam;
import study.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {   //사용자 정의 인터페이스

    //Where절에 메서드(파라미터)를 사용해 member 객체를 조회
    List<MemberTeamDto> search(MemberSearchParam param);

    //단순한 페이징, fetchResults() 사용 : 데이터 내용과 전체 카운트를 한번에 조회
    Page<MemberTeamDto> searchPageSimple(MemberSearchParam param,
                                         Pageable pageable);
//    Page<MemberTeamDto> searchPageSimple2(MemberSearchParam param,
//                                         Pageable pageable);

    //복잡한 페이징 - fetch()와 fetchCount() 사용 : 데이터 내용과 전체 카운트를 별도로 조회
    Page<MemberTeamDto> searchPageComplex(MemberSearchParam param,
                                          Pageable pageable);

}