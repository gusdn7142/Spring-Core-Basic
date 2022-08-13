package hello.upload.controller;


import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;



@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;


    //폼 템플릿 호출
    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    //서버에 파일 업로드 및 DB에 파일 저장
    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());           //첨부파일 업로드
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles()); //이미지 파일 업로드


        //Item DB에 상품명, 첨부파일, 이미지 파일 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}";
    }


    //뷰 템플릿 호출
    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {

        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);  //item 객체를 뷰에 넘김

        return "item-view";
    }


    //이미지 파일 다운로드 URL 리턴
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));  //ex, "file:/Users/../......" 으로 리턴
    }



    //첨부파일 리턴
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {

        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();   //서버에 저장된 파일명 (UUID 적용됨)
        String uploadFileName = item.getAttachFile().getUploadFileName();  //업로드한 파일명

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));  // 서버에 저장된 파일명을 통해 파일 다운로드 URL을 넘겨줌

        log.info("uploadFileName={}", uploadFileName);


        //다운로드시 파일명 설정 : "attachment; filename=" "
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);   //한글깨짐 방지를 위해 설정
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        //        String contentDisposition = "attachment; filename=\"" + uploadFileName + "\"";


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }


    


}
