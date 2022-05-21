package hello.core.singleton;




public class SingletonService {


    private static final SingletonService instance = new SingletonService();  //자기 자신을 하나만 존재하게 함

    private SingletonService() {
    }

    public static SingletonService getInstance() {
        return instance;
    }

    public void logic() {
        System.out.println("called singleton");
    }
}
