package study.querydsl.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.MemberSearchParam;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.repository.support.Querydsl4RepositorySupport;

import java.util.List;

import static org.hibernate.internal.util.StringHelper.isEmpty;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@Repository
public class MemberTestRepository extends Querydsl4RepositorySupport {

    public MemberTestRepository() {
        super(Member.class);
    }

    public List<Member> basicSelect() {
        return select(member)
                .from(member)
                .fetch();
    }

    public List<Member> basicSelectFrom() {
        return selectFrom(member)
                .fetch();
    }


    public Page<Member> searchPageByApplyPage(MemberSearchParam param,
                                              Pageable pageable) {

        JPAQuery<Member> query = selectFrom(member)
                .leftJoin(member.team, team)
                .where(usernameEq(param.getUsername()),
                        teamNameEq(param.getTeamName()),
                        ageGoe(param.getAgeGoe()),
                        ageLoe(param.getAgeLoe()));


        List<Member> content = getQuerydsl().applyPagination(pageable, query)
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable,
                query::fetchCount);

    }



    public Page<Member> applyPagination(MemberSearchParam param,
                                        Pageable pageable) {

        return applyPagination(pageable, contentQuery -> contentQuery
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(usernameEq(param.getUsername()),
                        teamNameEq(param.getTeamName()),
                        ageGoe(param.getAgeGoe()),
                        ageLoe(param.getAgeLoe())));
    }


    public Page<Member> applyPagination2(MemberSearchParam param,
                                         Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .selectFrom(member)
                        .leftJoin(member.team, team)
                        .where(usernameEq(param.getUsername()),
                                teamNameEq(param.getTeamName()),
                                ageGoe(param.getAgeGoe()),
                                ageLoe(param.getAgeLoe())),
                countQuery -> countQuery
                        .selectFrom(member)
                        .leftJoin(member.team, team)
                        .where(usernameEq(param.getUsername()),
                                teamNameEq(param.getTeamName()),
                                ageGoe(param.getAgeGoe()),
                                ageLoe(param.getAgeLoe()))
        );
    }

    private BooleanExpression usernameEq(String username) {
        return isEmpty(username) ? null : member.username.eq(username);
    }
    private BooleanExpression teamNameEq(String teamName) {
        return isEmpty(teamName) ? null : team.name.eq(teamName);
    }
    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe == null ? null : member.age.goe(ageGoe);
    }
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe == null ? null : member.age.loe(ageLoe);
    }
}