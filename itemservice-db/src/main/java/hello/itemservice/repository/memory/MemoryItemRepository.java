package hello.itemservice.repository.memory;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryItemRepository implements ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); //static..  DB대용 저장소
    private static long sequence = 0L;                            //static..  기본키로 사용

    @Override
    public Item save(Item item) {
        item.setId(++sequence);          //기본키 지정
        store.put(item.getId(), item);  //기본키, item 객체로 저장
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = findById(itemId).orElseThrow();   //idx로 Item 객체 조회
        findItem.setItemName(updateParam.getItemName());   //Item 객체의 아이템명 셋팅
        findItem.setPrice(updateParam.getPrice());   //Item 객체의 가격 셋팅
        findItem.setQuantity(updateParam.getQuantity());   //Item 객체의 수량 셋팅
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(store.get(id));  //기본키로 Item 객체 조회...   null일 경우도 가능하도록 함
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();   //ItemSearchCond DTO 객체에서 아이템명 조회
        Integer maxPrice = cond.getMaxPrice();  //ItemSearchCond DTO 객체에서 최대가격 조회

        return store.values().stream()          //저장소에서 값을 하나씩 조회하면서     //List<Item> collect=
                .filter(item -> {
                    if (ObjectUtils.isEmpty(itemName)) {  //아이템명이 비어있으면 통과
                        return true;
                    }
                    return item.getItemName().contains(itemName);    //아이템명이 있으면  조회한 아이템명과 같을때만 통과
                }).filter(item -> {
                    if (maxPrice == null) {  //아이템명이 있고, 최대가격이 null이면 통과
                        return true;
                    }
                    return item.getPrice() <= maxPrice;    //최대가격보다 아이템 가격이 작거나 같으면 통과
                })
                .collect(Collectors.toList());   //리스트로 반환
    }

    public void clearStore() {
        store.clear();   //저장소 초기화
    }

}
