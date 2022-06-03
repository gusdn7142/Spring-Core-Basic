package coustom_annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;





@Target(value = ElementType.TYPE )   //정보유지되는 대상 설정
@Retention(value = RetentionPolicy.RUNTIME)  //적용대상 설정
public @interface CustomAnnotation {

    String nickNmae(); //default "커스텀 메서드 출력!!!";
    String address();
}
