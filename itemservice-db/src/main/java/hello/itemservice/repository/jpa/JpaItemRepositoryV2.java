package hello.itemservice.repository.jpa;


import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {

    private final SpringDataJpaItemRepository repository;

    //아이템 저장
    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    //아이템 업데이트
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {

        Item findItem = repository.findById(itemId).orElseThrow();   //객체의 값이 없으면 throw new NoSuchElementException("No value present") 발생

        findItem.setItemName(updateParam.getItemName());  //영속성 컨텍스트의 1차 캐시에 저장된 Item 객체의 필드 값 수정
        findItem.setPrice(updateParam.getPrice());        //영속성 컨텍스트의 1차 캐시에 저장된 Item 객체의 필드 값 수정
        findItem.setQuantity(updateParam.getQuantity());  //영속성 컨텍스트의 1차 캐시에 저장된 Item 객체의 필드 값 수정
    }

    //아이템 단건 조회
    @Override
    public Optional<Item> findById(Long id) {
        return repository.findById(id);
    }



    //아이템 모두 조회 (+검색조건)
    @Override
    public List<Item> findAll(ItemSearchCond cond) {

        String itemName = cond.getItemName();     //ItemSearchCond DTO 객체의  ItemName 값 조회
        Integer maxPrice = cond.getMaxPrice();    //ItemSearchCond DTO 객체의  MaxPrice 값 조회


        if (StringUtils.hasText(itemName) && maxPrice != null) {     //상품명과 최대가격이 null이 아니면
            //return repository.findByItemNameLikeAndPriceLessThanEqual("%" + itemName + "%", maxPrice);
            return repository.findItems("%" + itemName + "%", maxPrice);
        } else if (StringUtils.hasText(itemName)) {             //상품명이 null이 아니면
            return repository.findByItemNameLike("%" + itemName + "%");
        } else if (maxPrice != null) {                       //최대가격이 null이 아니면
            return repository.findByPriceLessThanEqual(maxPrice);
        } else {                                //상품명과 최대가격이 모두 null이면
            return repository.findAll();
        }
    }




}