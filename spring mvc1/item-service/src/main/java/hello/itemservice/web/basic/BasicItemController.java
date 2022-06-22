package hello.itemservice.web.basic;


import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;



@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor   //final이 붙은 생성자 자동 생성
public class BasicItemController {

    private final ItemRepository itemRepository;

//    @Autowired
//    public BasicItemController (ItemRepository itemRepository){
//        this.itemRepository = itemRepository;
//    }

    /* 상품 전체 조회 */
    @GetMapping
    public String items(Model model) {

        //모든 상품 조회
        List<Item> items = itemRepository.findAll();

        //model 객체에 리스트 형식 items 객체 삽입
        model.addAttribute("items", items);
        return "basic/items";  //basic/items 논리 뷰 경로를 리턴
    }

    /* 상품 상세 조회 */
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {

        //상품 1개 조회
        Item item = itemRepository.findById(itemId);

        //model 객체에  item 객체 삽입
        model.addAttribute("item", item);

        return "basic/item";  //basic/item 논리 뷰 경로를 리턴
    }



    /* (form 전송 전) 상품 등록 폼 */
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";   //basic/addForm 논리 뷰 경로를 리턴
    }




    /* (form 전송 후) 상품 등록 폼 */
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {

        //상품 DB에 값 저장
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        //model 객체에  item 객체 삽입
        model.addAttribute("item", item);

        return "basic/item";  //basic/item 논리 뷰 경로 (상품 상세 목록 조회 페이지) 를 리턴
    }



    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {

        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능

        return "basic/item";
    }




    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }



    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }



//
//
////    //    @PostMapping("/add")
////    public String addItemV5(Item item) {
////        itemRepository.save(item);
////        return "redirect:/basic/items/" + item.getId();
////    }
//
//
//
////    @PostMapping("/add")
////    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
////        Item savedItem = itemRepository.save(item);
////        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/basic/items/{itemId}";
//    }



    /* (form 전송 전) 상품 수정 폼 */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {

        //상품 1개 조회
        Item item = itemRepository.findById(itemId);

        //model 객체에  item 객체 삽입
        model.addAttribute("item", item);

        return "basic/editForm"; //basic/editForm 논리 뷰 경로를 리턴
    }


    /* (form 전송 후) 상품 수정 폼 */
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {

        //상품 정보 업데이트
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";  //상세페이지 논리뷰 경로로 리다이렉트
    }




    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct   //초기화 콜백 메서드 수행 (상품 데이터 DB에 저장)
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }




}