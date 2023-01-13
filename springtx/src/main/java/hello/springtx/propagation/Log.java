package hello.springtx.propagation;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@Getter
@Setter
public class Log {

    @Id
    @GeneratedValue
    private Long id;     //기본키

    private String message;  //로그 메시지

    //JPA 스펙상 기본 생성자 필요
    public Log() {
    }

    public Log(String message) {
        this.message = message;
    }

}