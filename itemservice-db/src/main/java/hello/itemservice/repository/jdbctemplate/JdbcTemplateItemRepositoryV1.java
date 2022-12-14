package hello.itemservice.repository.jdbctemplate;


import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate
 */
@Slf4j
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

    private final JdbcTemplate template;    //JdbcTemplate 참조변수 선언

    //생성자
    public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }


    //아이템 저장
    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values (?,?,?)";    //SQL 작성
        KeyHolder keyHolder = new GeneratedKeyHolder();                                //DB에서 사용할 기본 키를 KeyHolder로 조회

        template.update(connection -> {        //영향 받은 row 수 반환
            //PreparedStatement 생성 (+자동 증가 키)
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();   //키 홀더에서 기본키를 조회
        item.setId(key);                               //item 객체에 기본키를 저장
        return item;
    }

    //아이템 업데이트
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=?, price=?, quantity=? where id=?";    //SQL 작성
        template.update(sql,                     //sql
                updateParam.getItemName(),       //파라미터1
                updateParam.getPrice(),          //파라미터2
                updateParam.getQuantity(),       //파라미터3
                itemId);                        //파라미터4 (기본키)
    }

    //DTO 객체 (형식1) : setter() 사용
    private RowMapper<Item> itemRowMapper() {
        return ((rs, rowNum) -> {
            Item item = new Item();                                       //Item DTO 객체 생성
            item.setId(rs.getLong("id"));                     //칼럼 값을 DTO 객체에 저장
            item.setItemName(rs.getString("item_name"));      //칼럼 값을 DTO 객체에 저장
            item.setPrice(rs.getInt("price"));               //칼럼 값을 DTO 객체에 저장
            item.setQuantity(rs.getInt("quantity"));         //칼럼 값을 DTO 객체에 저장
            return item;   //Item DTO 객체 반환
        });
    }

    //DTO 객체 (형식2) : 생성자 사용
//    private RowMapper<Item> itemRowMapper() {
//        return (rs, rowNum) ->  new Item(
//                rs.getString("item_name"),
//                rs.getInt("price"),
//                rs.getInt("quantity")
////                rs.getLong("id")
//        );
//    }


    //아이템 단건 조회
    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = ?";    //SQL 작성
        try {
            Item item = template.queryForObject(sql, itemRowMapper(), id);    //단건 조회  (sql, DTO, 파라미터)
            return Optional.of(item);                                         //item을 Optional로 반환
        } catch (EmptyResultDataAccessException e) {                       //EmptyResultDataAccessException : queryForObject()는 결과가 NULL 이면 이 예외가 터집니다.
            return Optional.empty();
        }
    }

    //아이템 모두 조회 (+검색조건)
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();     //검색조건 파라미터(아이템 명) 조회
        Integer maxPrice = cond.getMaxPrice();     //검색조건 파라미터(최대 가격) 조회

        String sql = "select id, item_name, price, quantity from item";   //SQL 작성

        //동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {   //itemName이나 maxPrice가 null이 아니면
            sql += " where";                                        //sql에 where 추가
        }

        boolean andFlag = false;
        List<Object> param = new ArrayList<>();

        //상품명으로 검색
        if (StringUtils.hasText(itemName)) {           //itemName이 null이 아니면
            sql += " item_name like concat('%',?,'%')";   //sql 추가
            param.add(itemName);                          //파라미터 추가
            andFlag = true;                              //andFlag를 true로 변경
        }

        //최대가격 or 상품명과 최대가격으로 검색
        if (maxPrice != null) {           //maxPrice가 null이 아니면
            if (andFlag) {                //andFlag가 true이면
                sql += " and";           //sql에 and추가
            }
            sql += " price <= ?";      //andFlag가 false이면  sql에 price <= ? 추가
            param.add(maxPrice);        //파라미터 추가
        }

        log.info("sql={}", sql);
        return template.query(sql, itemRowMapper(), param.toArray());   //Select 쿼리 실행 (sql, DTO 객체,  파라미터)
    }


}
