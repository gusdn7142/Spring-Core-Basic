package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {

    /**
     * @param paramMap
     * @param model
     * @return viewName
     */

    //viewName만 리턴
    String process(Map<String, String> paramMap, Map<String, Object> model);

}
