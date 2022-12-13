package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.memory.MemoryItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfig {

    //ItemServiceV1 수동 빈 등록, 생성자로 의존관계 주입
    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    //MemoryItemRepository 수동 빈 등록, 생성자로 의존관계 주입
    @Bean
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }

}
