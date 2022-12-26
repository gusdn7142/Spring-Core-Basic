package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

    //생략 가능
    //List<Item> findAll();

    //아이템 이름이 포함되는 Item 객체 조히
    List<Item> findByItemNameLike(String itemName);    //select i from Item i where i.name like ?

    //해당 가격 이하인 Item 객체 조회
    List<Item> findByPriceLessThanEqual(Integer price);   //select i from Item i where i.price <= ?

    //아이템 이름이 포함되고 해당 가격 이하인 Item 객체 리스트 조회 (쿼리 자동 생성)
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    //아이템 이름이 포함되고 해당 가격 이하인 Item 객체 리스트 조회 (쿼리 수동 생성)
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);



}