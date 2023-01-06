package hello.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired  //의존성 주입
    OrderService orderService;

    @Autowired   //의존성 주입
    OrderRepository orderRepository;


    @Test
    //주문 테스트 - 정상
    void complete() throws NotEnoughMoneyException {
        //given
        Order order = new Order();   //"정상" 주문 생성
        order.setUsername("정상");

        //when
        orderService.order(order);   //주문 등록

        //then
        Order findOrder = orderRepository.findById(order.getId()).get();  //주문 조회
        assertThat(findOrder.getPayStatus()).isEqualTo("완료");   //주문 검증
    }


    //주문 테스트 - 시스템 예외 (런타임 예외) 발생
    @Test
    void runtimeException() {

        //given
        Order order = new Order();    //시스템 예외 주문 생성
        order.setUsername("예외");

        //when, then
        assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(RuntimeException.class);   //주문 등록(예외발생 후 롤백)시 RuntimeException 예외 발생 확인


        //then: 롤백되었으므로 데이터가 없어야 한다.
        Optional<Order> orderOptional = orderRepository.findById(order.getId());   //null인 주문이 조회되는 것을 검증
        assertThat(orderOptional.isEmpty()).isTrue();
    }


    //주문 테스트 - 비즈니스 예외 (체크 예외) 발생
    @Test
    void bizException() {

        //given
        Order order = new Order();   //비즈니스 예외 주문 생성
        order.setUsername("잔고부족");

        //when
        try {
            orderService.order(order);    //주문 등록 (예외발생 후 커밋)
        } catch (NotEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내");
        }

        //then
        Order findOrder = orderRepository.findById(order.getId()).get();   //주문 조회
        assertThat(findOrder.getPayStatus()).isEqualTo("대기");     //결제 상태 확인
    }


}