package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jdbctemplate.JdbcTemplateItemRepositoryV1;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration             //Bean 설정 파일로 등록 (스프링 실행시 등록됨.)
@RequiredArgsConstructor   //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class JdbcTemplateV1Config {

    private final DataSource dataSource;   //DataSource 참조변수 선언

    @Bean   //메소드 명으로 Bean 등록
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());     //의존성 주입
    }

    @Bean   //메소드 명으로 Bean 등록
    public ItemRepository itemRepository() {
        return new JdbcTemplateItemRepositoryV1(dataSource);   //의존성 주입
    }

}
