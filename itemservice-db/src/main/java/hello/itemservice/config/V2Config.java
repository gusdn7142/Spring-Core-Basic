package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.JpaItemRepositoryV3;
import hello.itemservice.repository.v2.ItemQueryRepositoryV2;
import hello.itemservice.repository.v2.ItemRepositoryV2;
import hello.itemservice.repository.v2.ItemServiceV2;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;


@Configuration             //Bean 설정 파일로 등록 (스프링 실행시 등록됨.)
@RequiredArgsConstructor   //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class V2Config {

    private final EntityManager entityManager;        //EntityManager 참조변수 선언
    private final ItemRepositoryV2 itemRepositoryV2;   //SpringDataJPA


    @Bean   //메소드 명으로 Bean 등록
    public ItemService itemService() {
        return new ItemServiceV2(itemRepositoryV2, itemQueryRepositoryV2());     //의존성 주입
    }

    @Bean  //메소드 명으로 Bean 등록
    public ItemQueryRepositoryV2 itemQueryRepositoryV2() {
        return new ItemQueryRepositoryV2(entityManager);   //의존성 주입
    }

    //ItemRepositoryV2는 @Repository 어노테이션이 자동으로 Bean 등록을 해줍니다.


    @Bean   //메소드 명으로 Bean 등록..  Test시 활용
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(entityManager);   //의존성 주입
    }

}