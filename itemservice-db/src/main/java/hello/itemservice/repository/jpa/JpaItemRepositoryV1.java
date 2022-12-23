package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Slf4j
@Repository
@Transactional        //JPA의 모든 변경(등록, 수정, 삭제)은 트랜잭션 안에서 이루어 진다.
public class JpaItemRepositoryV1 implements ItemRepository {


    private final EntityManager em;                //EntityManager 참조변수 선언

    public JpaItemRepositoryV1(EntityManager em) {
        this.em = em;                              //EntityManager에 의존성 주입
    }

    //아이템 저장
    //@Transactional
    @Override
    public Item save(Item item) {
        em.persist(item);        //item 객체 저장 (영속성 컨텍스트에 저장)
        return item;
    }

    //아이템 업데이트
    //@Transactional
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);      //Item 객체에서 itemId를 통해  Item 객체 조회

        findItem.setItemName(updateParam.getItemName());   //item 객체에 updateParam DTO 객체의 ItemName 필드 저장
        findItem.setPrice(updateParam.getPrice());          //item 객체에 updateParam DTO 객체의 price 필드 저장
        findItem.setQuantity(updateParam.getQuantity());   //item 객체에 updateParam DTO 객체의 quantity 필드 저장
    }

    //아이템 단건 조회
    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);      //Item 객체에서 id를 통해  Item 객체 조회

        return Optional.ofNullable(item);   //item 겍체를 Optional 타입으로 씌워서 리턴
    }

    //아이템 모두 조회(+검섹 조건)
    @Override
    public List<Item> findAll(ItemSearchCond cond) {

        String jpql = "select i from Item i";    //엔티티 기반의 jpql 쿼리 작성

        Integer maxPrice = cond.getMaxPrice();   //ItemSearchCond DTO 객체의  MaxPrice 값 조회
        String itemName = cond.getItemName();   //ItemSearchCond DTO 객체의  ItemName 값 조회


        //동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where";
        }

        //상품명으로 검색
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            jpql += " i.itemName like concat('%',:itemName,'%')";   //jpql 추가 (+파라미터 추가)
            andFlag = true;                                          //andFlag를 true로 변경
        }

        //최대가격 or 상품명과 최대가격으로 검색
        if (maxPrice != null) {                //maxPrice가 null이 아니면
            if (andFlag) {                     //andFlag가 true이면
                jpql += " and";                //jpql에 and추가
            }
            jpql += " i.price <= :maxPrice";   //andFlag가 false이면  jpql에 price <= ? 추가 (+파라미터 추가)
        }
        log.info("jpql={}", jpql);

        TypedQuery<Item> query = em.createQuery(jpql, Item.class);   //jpql 쿼리 생성

        if (StringUtils.hasText(itemName)) {                   //itemName 필드가 null이 아니면
            query.setParameter("itemName", itemName);     //jpal에 itemName 필드 삽입
        }
        if (maxPrice != null) {                                 //maxPrice 필드가 null이 아니면
            query.setParameter("maxPrice", maxPrice);     //jpal에 maxPrice 필드 삽입
        }

        return query.getResultList();                           //jpal 쿼리 결과 리턴...  결과가 하나 이상일 때 (리스트 반환)
    }



}