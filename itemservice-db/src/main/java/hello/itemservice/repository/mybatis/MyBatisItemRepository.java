package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



//ItemMapper의 구현체 클래스
@Slf4j
@Repository
@RequiredArgsConstructor    //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class MyBatisItemRepository implements ItemRepository {


    private final ItemMapper itemMapper;   //ItemMapper 참조변수를 통해 의존관계를 자동 주입

    //아이템 저장
    @Override
    public Item save(Item item) {
        log.info("itemMapper class={}", itemMapper.getClass());
        itemMapper.save(item);
        return item;
    }

    //아이템 업데이트
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        itemMapper.update(itemId, updateParam);
    }

    //아이템 조회 (단건 조히)
    @Override
    public Optional<Item> findById(Long id) {
        return itemMapper.findById(id);
    }

    //아이템 전체 조회 (+검색 조건)
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        return itemMapper.findAll(cond);
    }


}
