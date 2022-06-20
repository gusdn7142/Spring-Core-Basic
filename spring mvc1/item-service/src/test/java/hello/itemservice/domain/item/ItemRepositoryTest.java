package hello.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;





class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach //각 테스트(@Test)가 끝날때마다 실행됨
    void afterEach(){
        itemRepository.clearStore();
    }


    /*
     * 상품 저장 테스트
     */
    @Test
    void save() {
        //given (준비)
        Item postItemReq = new Item("itemA", 10000, 10);  //Item 객체 생성

        //when (실행)
        Item savedItem = itemRepository.save(postItemReq);  //HashMap타입의 store 객체에 postItemReq 객체 저장

        //then (검증)
        Item findItem = itemRepository.findById(postItemReq.getId());  //postItemReq 객체에서 id값을 가져와 상품 1개 조회
        assertThat(findItem).isEqualTo(savedItem);  //HashMap타입의 store에 저장된 item 객체와 postItemReq객체의 id값으로 가져온 item 객체가 일치하는지 비교
    }



    /*
    * 상품 조회 테스트
    */
    @Test
    void findAll() {

        //given (준비)
        Item postItemReq1 = new Item("item1", 10000, 10);   //postItemReq1객체 생성
        Item postItemReq2 = new Item("item2", 20000, 20);

        itemRepository.save(postItemReq1);   //HashMap타입의 store 객체에 postItemReq1 객체 저장
        itemRepository.save(postItemReq2);

        //when (실행)
        List<Item> result = itemRepository.findAll();  //store 객체에 저장된 모든 value값 (item) 객체 조회

        //then (검증)
        assertThat(result.size()).isEqualTo(2);  //result의 크기가 2인지 확인
        assertThat(result).contains(postItemReq1, postItemReq2);  //result 리스트에 postItemReq1와 postItemReq2 객체가 있는지 확인
    }


    /*
     * 상품 수정 테스트
     */
    @Test
    void updateItem() {
        //given (준비)
        Item postItemReq = new Item("item1", 10000, 10); //postItemReq객체 생성

        Item savedItem = itemRepository.save(postItemReq); //HashMap타입의 store객체에 postItemReq 객체 저장
        Long itemId = savedItem.getId();                     //item객체에서 id값을 불러옴


        //when (실행)
        Item patchupdateReq = new Item("item2", 20000, 30);  //patchupdateReq객체 생성
        itemRepository.update(itemId, patchupdateReq);       //item 객체의 필드값들을 patchupdateReq객체의 값으로 대체

        Item findItem = itemRepository.findById(itemId);   //item 객체 조회

        //then (테스트)
        assertThat(findItem.getItemName()).isEqualTo(patchupdateReq.getItemName());  //수정 전 patchupdateReq 객체와 DB에 수정후 불러온 findItem 객체 비교
        assertThat(findItem.getPrice()).isEqualTo(patchupdateReq.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(patchupdateReq.getQuantity());
    }






}