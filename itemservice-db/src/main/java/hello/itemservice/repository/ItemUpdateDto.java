package hello.itemservice.repository;

import lombok.Data;

@Data
public class ItemUpdateDto {    //아이템 업데이트 DTO
    private String itemName;   //아이템 명
    private Integer price;    //가격
    private Integer quantity;  //수량

    public ItemUpdateDto() {
    }

    public ItemUpdateDto(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
