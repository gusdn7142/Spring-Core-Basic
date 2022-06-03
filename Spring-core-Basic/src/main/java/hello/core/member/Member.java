package hello.core.member;




public class Member {

    private Long id;      //인덱스
    private String name;  //이름
    private Grade grade;  //성적



    public Member() {
    }

    public Member(Long id, String name, Grade grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    /* 기본 생성자 생성 */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    /* getter, setter 생성 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }



}
