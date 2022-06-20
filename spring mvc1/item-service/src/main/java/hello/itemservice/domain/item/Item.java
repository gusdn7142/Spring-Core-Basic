package hello.itemservice.domain.item;


import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class Item {

    private Long id;         //상품 id
    private String itemName;  //상품 명
    private Integer price;     //상품 가격
    private Integer quantity;  //상품 수량


    //기본 생성자 생성
    public Item() {
    }


    //인자 있는 생성자 생성
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }



}
