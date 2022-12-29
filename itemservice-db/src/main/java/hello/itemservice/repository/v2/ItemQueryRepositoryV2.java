package hello.itemservice.repository.v2;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemSearchCond;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static hello.itemservice.domain.QItem.item;


@Repository
public class ItemQueryRepositoryV2 {

    private final JPAQueryFactory query;   //JPAQueryFactory 참조변수 선언


    public ItemQueryRepositoryV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);       //JPAQueryFactory에 EntityManager 의존성 주입
    }


    //아이템 모두 조회 (+검색조건) : 임의의 메서드 사용
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