package hello.itemservice.repository;

import lombok.Data;

@Data
public class ItemSearchCond {    //아이템 검색 조건 DTO

    private String itemName;   //아이템 명
    private Integer maxPrice;  //최대 가격

    public ItemSearchCond() {
    }

    public ItemSearchCond(String itemName, Integer maxPrice) {
        this.itemName = itemName;
        this.maxPrice = maxPrice;
    }
}
