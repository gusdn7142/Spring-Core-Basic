package hello.upload.domain;

import lombok.Data;



@Data
public class UploadFile {

    private String uploadFileName;   //업로드한 파일 이름
    private String storeFileName;    //시스템에 저장할 파일이름 (UUID 적용됨)

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }



}
