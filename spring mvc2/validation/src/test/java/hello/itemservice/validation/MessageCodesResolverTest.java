package hello.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;




public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();   //MessageCodesResolver 인터페이스는 여러개의 에러코드 값들을 반환해줌


    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");

        for (String messageCode: messageCodes){
            System.out.println("messageCode = " + messageCode);
        }
        //bindingResult.rejectValue("required");

        assertThat(messageCodes).containsExactly("required.item", "required");
    }


    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);


        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        //bindingResult.rejectValue("itemName","required");


        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }






}
