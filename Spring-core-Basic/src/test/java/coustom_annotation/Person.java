package coustom_annotation;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@CustomAnnotation(nickNmae = "뎁스", address = "abc")
public class Person {

    private String name;
    private int age;



//    public Person(String name, int age) {
//        super();
//        this.name = name;
//        this.age = age;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public int getAge() {
//        return age;
//    }
//    public void setAge(int age) {
//        this.age = age;
//    }









}
