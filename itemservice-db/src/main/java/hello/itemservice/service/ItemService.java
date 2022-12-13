package hello.itemservice.service;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    //아이템 저장
    Item save(Item item);

    //아이템 업데이트
    void update(Long itemId, ItemUpdateDto updateParam);

    //아이템 단건 조회
    Optional<Item> findById(Long id);

    //아이템 모두 조회 (+검색 조건)
    List<Item> findItems(ItemSearchCond itemSearch);
}
