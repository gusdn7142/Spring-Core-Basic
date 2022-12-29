package hello.itemservice.repository.v2;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import hello.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceV2 implements ItemService {

    private final ItemRepositoryV2 itemRepositoryV2;               //ItemRepositoryV2 참조변수 선언
    private final ItemQueryRepositoryV2 itemQueryRepositoryV2;    //ItemQueryRepositoryV2 참조변수 선언

    //아이템 저장
    @Override
    public Item save(Item item) {
        return itemRepositoryV2.save(item);
    }

    //아이템 업데이트
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = findById(itemId).orElseThrow();   //객체의 값이 없으면 throw new NoSuchElementException("No value present") 발생

        findItem.setItemName(updateParam.getItemName());  //영속성 컨텍스트의 1차 캐시에 저장된 Item 객체의 필드 값 수정
        findItem.setPrice(updateParam.getPrice());        //영속성 컨텍스트의 1차 캐시에 저장된 Item 객체의 필드 값 수정
        findItem.setQuantity(updateParam.getQuantity());  //영속성 컨텍스트의 1차 캐시에 저장된 Item 객체의 필드 값 수정
    }

    //아이템 단건 조회
    @Override
    public Optional<Item> findById(Long id) {
        return itemRepositoryV2.findById(id);
    }


    //아이템 모두 조회 (+검색 조건)
    @Override
    public List<Item> findItems(ItemSearchCond cond) {
        return itemQueryRepositoryV2.findAll(cond);
    }



}