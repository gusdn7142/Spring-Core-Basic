package coustom_annotation;


import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.awt.print.Book;
import java.lang.annotation.Annotation;
import java.util.Arrays;

public class PersonService {

    //PersonService p = new PersonService();

    @Test
    void test() {

        Person person = new Person("kim", 20);

        //System.out.println(person.getAge());
        //System.out.println(person.getName());

        //CustomAnnotation customAnnotation = (CustomAnnotation) instanceof CustomAnnotation;

//        for(Annotation a : Person.class.getAnnotations()){
//            System.out.println(a.nickName);
//        }

        //Arrays.stream(Person.class.getDeclaredAnnotations()).forEach(System.out::println);

        Annotation[] annotations = Person.class.getAnnotations();  //Person클래스의 설정된 모든 어노테이션을 불러와
        //Declared


        for(Annotation annotation : annotations) {  //어노테이션을 하나 뽑아
//            if (annotation instanceof CustomAnnotation) {
            CustomAnnotation personInfo = (CustomAnnotation) annotation;  //coustom Annotation 선언
//            System.out.println(personInfo.value());
                System.out.println(personInfo.nickNmae() + " , " + personInfo.address() );
//            }
        }





    }


}
