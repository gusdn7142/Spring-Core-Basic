package study.querydsl.dto;

import lombok.Data;


@Data
public class MemberSearchParam {

    private String username;  //회원명
    private String teamName;  //팀명
    private Integer ageGoe;   //나이 >= ageGoe
    private Integer ageLoe;   //나이 <= ageLoe


}