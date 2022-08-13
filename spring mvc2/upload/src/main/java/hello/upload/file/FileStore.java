package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;




@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;


    public String getFullPath(String filename) {   //전체 경로 반환
        return fileDir + filename;
    }

    //파일 여러개 업로드
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {

        List<UploadFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {  //파일이 존재하면
                storeFileResult.add(storeFile(multipartFile));  //파일 업로드 여러개를 수행한 리스트에 담는다.
            }
        }

        return storeFileResult;
    }


    //파일 1개 업로드
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {  //파일이 존재하지 않으면!
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();

        //파일명을 통해 uuid 파일명 생성
        String storeFileName = createStoreFileName(originalFilename);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));  //(디렉터리+파일명)을 통해 파일 업로드 실행!!!

        return new UploadFile(originalFilename, storeFileName);   //업로드할 파일명과 저장소에 저장할 파일명을 리턴
    }




    //파일명을 UUID로 변경
    private String createStoreFileName(String originalFilename) {

        String ext = extractExt(originalFilename);  //파일명에서 확장자만 추출
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;                    //uuid와 확장자를 붙여서 리턴
    }



    //파일명에서 확장자만 뽑기
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");  //확장자 앞 .(점)의 인덱스를 뽑음
        return originalFilename.substring(pos + 1);   //확장자명만 추출
    }




}
