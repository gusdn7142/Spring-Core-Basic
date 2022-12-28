package hello.itemservice.repository.jpa;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.*;

@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {


    private final EntityManager em;        //EntityManager 참조변수 선언
    private final JPAQueryFactory query;   //JPAQueryFactory 참조변수 선언


    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);       //JPAQueryFactory에 EntityManager 의존성 주입
    }

    //아이템 저장
    @Override
    public Item save(Item item) {
        em.persist(item);                          //item 객체 저장 (영속성 컨텍스트에 저장)
        return item;
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
        Item item = em.find(Item.class, id);      //Item 객체에서 id를 통해  Item 객체 조회
        return Optional.ofNullable(item);         //item 겍체를 Optional 타입으로 씌워서 리턴
    }


    //아이템 모두 조회 (+검색조건) : BooleanBuilder 사용
    public List<Item> findAllOld(ItemSearchCond itemSearch) {

        String itemName = itemSearch.getItemName();   //ItemSearchCond DTO 객체의  ItemName 값 조회
        Integer maxPrice = itemSearch.getMaxPrice();   //ItemSearchCond DTO 객체의  MaxPrice 값 조회


        QItem item = QItem.item;   //QItem 객체 생성

        BooleanBuilder builder = new BooleanBuilder();                //BooleanBuilder 객체 생성

        if (StringUtils.hasText(itemName)) {                            //itemName이 null이 아니면
            builder.and(item.itemName.like("%" + itemName + "%"));  //Qitem의  itemName필드의 조건을 다음과 같이 설정
        }

        if (maxPrice != null) {                           //itemName이 null이 아니면
            builder.and(item.price.loe(maxPrice));        //Qitem의  price필드의 조건을 다음과 같이 설정
        }

        //QueryDSL로 JPQL 생성 및 실행 (+동적쿼리 추가)
        List<Item> result = query
                .select(item)
                .from(item)
                .where(builder)
                .fetch();
        return result;
    }




    //아이템 모두 조회 (+검색조건) : 임의의 메서드 사용
    @Override
    public List<Item> findAll(ItemSearchCond cond) {

        String itemName = cond.getItemName();   //ItemSearchCond DTO 객체의  ItemName 값 조회
        Integer maxPrice = cond.getMaxPrice();   //ItemSearchCond DTO 객체의  MaxPrice 값 조회

        //QItem item = QItem.item;   //QItem 객체 생성


        //QueryDSL로 JPQL 생성 및 실행 (+동적쿼리 추가)
        List<Item> result = query
                .select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();
        return result;
    }



    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {                 //itemName이 null이 아니면
            return item.itemName.like("%" + itemName + "%");  //Qitem의  itemName필드의 조건을 다음과 같이 설정
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {                //maxPrice가 null이 아니면
            return item.price.loe(maxPrice);  //Qitem의  price필드의 조건을 다음과 같이 설정
        }
        return null;
    }


}
