package hello.itemservice.domain.item;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository  //컴포넌트 스캔 대상
public class ItemRepository {



    private static final Map<Long, Item> store = new HashMap<>();  //저장소(DB 역할)를 Map으로 선언 (상품 Id, 상품 객체)
    private static long sequence = 0L; //id값으로 사용 예정



    //상품 저장 함수
    public Item save(Item item) {
        item.setId(++sequence);           //id값을 1씩 증가시키면서 저장 (autoincrement 역할)
        store.put(item.getId(), item);    //store DB에 id와 상품 객체 저장
        return item;                       //상품 객체 반환
    }

    //상품 1개 조회
    public Item findById(Long id) {
        return store.get(id);
    }


    //전체 상품 조회
    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    //상품 수정
    public void update(Long itemId, Item updateParam) {  //상품 id와 상품 Request DTO 갹체 입력

        //상품 1개 조회
        Item findItem = findById(itemId);

        //상품 정보 수정
        findItem.setItemName(updateParam.getItemName());  //상품 Request DTO 갹체에서 받아온 값 입력
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

   //전체 상품 삭제
    public void clearStore() {
        store.clear();   //Hashmap인 store객체의 데이터를 다 날림
    }




}
