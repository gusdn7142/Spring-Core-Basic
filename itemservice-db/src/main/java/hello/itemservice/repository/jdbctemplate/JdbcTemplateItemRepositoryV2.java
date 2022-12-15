package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * NamedParameterJdbcTemplate
 * SqlParameterSource
 * - BeanPropertySqlParameterSource
 * - MapSqlParameterSource
 * Map
 *
 * BeanPropertyRowMapper
 *
 */
@Slf4j
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

    private final NamedParameterJdbcTemplate template;    //NamedParameterJdbcTemplate 참조변수 선언


    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    //아이템 저장
    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) " +    //SQL 작성 (? 대신 : 사용)
                "values (:itemName, :price, :quantity)";

        //DTO 객체를 통해 파라미터 객체 자동 생성
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);  //item 객체를 통해  파라미터(param) 생성

        KeyHolder keyHolder = new GeneratedKeyHolder();   //DB에서 사용할 기본 키를 KeyHolder로 조회
        template.update(sql, param, keyHolder);        //영향 받은 row 수 반환

        long key = keyHolder.getKey().longValue();   //키 홀더에서 기본키를 조회
        item.setId(key);                               //item 객체에 기본키를 저장
        return item;
    }

    //아이템 업데이트
    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item " +
                "set item_name=:itemName, price=:price, quantity=:quantity " +   //SQL 작성
                "where id=:id";

        //DTO 객체를 통해 파라미터 객체 수동 생성
        SqlParameterSource param = new MapSqlParameterSource()    //updateParam으로 파라미터(param) 생성
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId); //이 부분이 별도로 필요하다.

        template.update(sql, param);
    }


    //아이템 단건 조회
    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = :id";    //SQL 작성
        try {
            //Map을 통해 파라미터 객체 생성
            Map<String, Object> param = Map.of("id", id);             //id로 파라미터 생성

            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);                                         //item을 Optional로 반환
        } catch (EmptyResultDataAccessException e) {                         //EmptyResultDataAccessException : queryForObject()는 결과가 NULL 이면 이 예외가 터집니다.
            return Optional.empty();
        }
    }

    //아이템 모두 조회 (+검색조건)
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();     //검색조건 파라미터(아이템 명) 조회
        Integer maxPrice = cond.getMaxPrice();     //검색조건 파라미터(최대 가격) 조회

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);  //cond 객체를 통해  파라미터(param) 생성

        String sql = "select id, item_name, price, quantity from item";   //SQL 작성

        //동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {   //itemName이나 maxPrice가 null이 아니면
            sql += " where";
        }

        //상품명으로 검색
        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";   //sql 추가 (+파라미터 추가)
            andFlag = true;                              //andFlag를 true로 변경
        }
        //최대가격 or 상품명과 최대가격으로 검색
        if (maxPrice != null) {           //maxPrice가 null이 아니면
            if (andFlag) {                //andFlag가 true이면
                sql += " and";           //sql에 and추가
            }
            sql += " price <= :maxPrice";      //andFlag가 false이면  sql에 price <= ? 추가 (+파라미터 추가)
        }

        log.info("sql={}", sql);
        return template.query(sql, param, itemRowMapper());   //Select 쿼리 실행 (sql, DTO 객체,  파라미터)
    }


    //DTO 객체 (형식1)
    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class); //Item 타입으로 DTO 객체 생성
    }


}
