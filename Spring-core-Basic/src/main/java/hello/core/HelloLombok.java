package hello.core;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HelloLombok {


    private String name;
    private int age;


    public static void main(String[] args) {

        //getter, setter 사용
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("depth");

        String name = helloLombok.getName();
        System.out.println("name은 "  + name);

        //Tostring 적용 이후 (객체 내부 값 눈으로 확인 가능)
        System.out.println("hellogLombok " + helloLombok);


    }





}
