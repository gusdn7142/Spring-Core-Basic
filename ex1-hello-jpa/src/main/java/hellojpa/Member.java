package hellojpa;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;


@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)                     //하나의 팀이 여러명의 멤버를 가질 수 있음
    @JoinColumn(name = "TEAM_ID") //join하는 컬럼은 TEAM_ID  Team을 TEAM_ID로 join
    private Team team;

//    //기간 : Period
//    @Embedded
//    private Period workPeriod;

    //주소
    @Embedded
    private Address homeAddress;


    @ElementCollection  //값 타입 컬렉션
    @CollectionTable(name = "FAVORITE_FOODS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

//    //@OrderColumn(name = "address_history_order")
//    @ElementCollection   //값 타입 컬렉션
//    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
//    private List<Address> addressHistory = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();


    //주소
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "city",
//                        column=@Column(name="WORK_CITY")),
//            @AttributeOverride(name = "street",
//                        column=@Column(name="WORK_STREET")),
//            @AttributeOverride(name = "zipcode",
//                        column=@Column(name="WORK_ZIPCODE"))
//            })
//    private Address workAddress;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }
}
