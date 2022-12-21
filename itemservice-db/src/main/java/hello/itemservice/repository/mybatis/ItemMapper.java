package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

//매퍼 인터페이스 (구현체는 자동 생성됨)
@Mapper
public interface ItemMapper {

    //아이템 저장
    void save(Item item);

    //아이템 업데이트
    void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateParam);   //@Param이 하나이면 생략 가능!!

    //아이템 단건 조회
    Optional<Item> findById(Long id);

    //아이템 모두 조회 (+검색 조건)
    List<Item> findAll(ItemSearchCond itemSearch);


}
