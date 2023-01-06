package hello.springtx.order;

import org.springframework.data.jpa.repository.JpaRepository;


//레포지토리
public interface OrderRepository extends JpaRepository<Order, Long> {
}