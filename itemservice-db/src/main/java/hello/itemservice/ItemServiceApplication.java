package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
//@Import(JdbcTemplateV1Config.class)                                 //JdbcTemplateV1Config를 Bean 등록 설정파일로 사용
//@Import(JdbcTemplateV2Config.class)                                 //JdbcTemplateV2Config를 Bean 등록 설정파일로 사용
//@Import(JdbcTemplateV3Config.class)                                 //JdbcTemplateV3Config를 Bean 등록 설정파일로 사용
//@Import(MyBatisConfig.class)                                          //MyBatisConfig Bean 등록 설정파일로 사용
//@Import(JpaConfig.class)
//@Import(SpringDataJpaConfig.class)
@Import(QueryDslConfig.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")   //컴포넌트 스캔 범위 지정
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	//초기 데이터를 추가하는 TestDataInit 클래스를 빈으로 등록
	@Bean
	@Profile("local")   //특정 프로필("local")의 경우에만 해당 스프링 빈을 등록
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}


/*	@Bean
	@Profile("test")   //특정 프로필("test")의 경우에만 해당 스프링 빈을 등록
	public DataSource dataSource() {

		log.info("메모리 데이터베이스 초기화");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");   //데이터소스를 만들때 이렇게만 적으면 임베디드 모드(메모리 모드)로 동작하는 H2 데이터베이스를 사용
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}*/



}


