package hello.itemservice.domain;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import hello.itemservice.repository.memory.MemoryItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
//@Commit
//@Rollback(value = false)
@Transactional
@SpringBootTest              //@SpringBootApplication이 붙은 클래스를 찾아서 설정으로 사용
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;  //ItemRepository 인터페이스

/*    //트랜잭션 관련 코드
    @Autowired
    PlatformTransactionManager transactionManager;    //트랜잭션 매니저 참조변수 선언
    TransactionStatus status;                        //TransactionStatus 참조변수*/


/*    //@BeforeEach : 각각의 테스트의 실행이 시작되기 전에 호출
    @BeforeEach
    void beforeEach() {
        //트랜잭션 시작
        status = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }*/


/*    //@AfterEach : 각각의 테스트의 실행이 끝나는 시점에 호출
    @AfterEach
    void afterEach() {
        //MemoryItemRepository의 경우 제한적으로 사용
        if (itemRepository instanceof MemoryItemRepository) {   //itemRepository 인터페이스에는 clearStore() 메서드가 없기 때문에.. 다운캐스팅 수행...
            ((MemoryItemRepository) itemRepository).clearStore();  //저장소 초기화..
        }

        //트랜잭션 롤백
        transactionManager.rollback(status);
    }*/


    /*
     *1. 아이템을 하나 저장하고 잘 저장되었는지 검증
     */
    @Test
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);

        //when
        Item savedItem = itemRepository.save(item);   //아이템 저장

        //then
        Item findItem = itemRepository.findById(item.getId()).get();   //아이템 조회
        assertThat(findItem).isEqualTo(savedItem);  //검증
    }

    /*
     *2. 아이템을 하나 수정하고 잘 수정되었는지 검증
     */
    @Test
    void updateItem() {
        //given
        Item item = new Item("item1", 10000, 10);
        Item savedItem = itemRepository.save(item);   //아이템 저장
        Long itemId = savedItem.getId();             //아이템 조회

        //when
        ItemUpdateDto updateParam = new ItemUpdateDto("item2", 20000, 30);
        itemRepository.update(itemId, updateParam);   //아이템 업데이트

        //then
        Item findItem = itemRepository.findById(itemId).get();   //아이템 조회
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());  //검증
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }

    /*
     *3. 아이템을 찾는 테스트
     */
    @Test
    void findItems() {
        //given
        Item item1 = new Item("itemA-1", 10000, 10);
        Item item2 = new Item("itemA-2", 20000, 20);
        Item item3 = new Item("itemB-1", 30000, 30);

        log.info("repository={}",itemRepository.getClass());
        itemRepository.save(item1);   //아이템 저장
        itemRepository.save(item2);   //아이템 저장
        itemRepository.save(item3);   //아이템 저장

        //(itemName, maxPrice) 둘 다 없음 검증
        test(null, null, item1, item2, item3);   //검증1 : 아이템 명, 최대 가격,  Item 객체들...
        test("", null, item1, item2, item3);   //검증2 : 아이템 명, 최대 가격,  Item 객체들...

        //itemName 검증
        test("itemA", null, item1, item2);   //검증3 : 아이템 명, 최대 가격,  Item 객체들...
        test("temA", null, item1, item2);   //검증3 : 아이템 명, 최대 가격,  Item 객체들...
        test("itemB", null, item3);   //검증3 : 아이템 명, 최대 가격,  Item 객체들...

        //maxPrice 검증
        test(null, 10000, item1);   //부분 검색

        //둘 다 있음 검증
        test("itemA", 10000, item1);   //부분 검색
    }

    void test(String itemName, Integer maxPrice, Item... items) {
        List<Item> result = itemRepository.findAll(new ItemSearchCond(itemName, maxPrice));  //아이템명과 최대가격으로 item 객체 조회
        assertThat(result).containsExactly(items);         //아이템들이 순서에 맞게 조회되었는지 검증..
    }


}
