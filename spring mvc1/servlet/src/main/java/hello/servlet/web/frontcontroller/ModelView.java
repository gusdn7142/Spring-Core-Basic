package hello.servlet.web.frontcontroller;


import java.util.HashMap;
import java.util.Map;

public class ModelView {


    private String viewName;  //논리적인 뷰 이름
    private Map<String, Object> model = new HashMap<>();  //response 모델 생성

    //model 생성자
    public ModelView(String viewName) {
        this.viewName = viewName;
    }



    //뷰 이름 반환 getter()
    public String getViewName() {
        return viewName;
    }

    //뷰 이름 삽입 setter()
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    //model 반환 getter()
    public Map<String, Object> getModel() {
        return model;
    }

    //model 삽입 setter()
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }



}
