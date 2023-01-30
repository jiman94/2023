
```java
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import com.file.example.repository.FileUploadRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 유틸 클래스
 */
@Component
public class FileUtil {
    // 서버 업로드 경로
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    // 최대 파일 크기
    private long MAX_SIZE = 52428800;

    // 파일 정보 저장 시, 레파지토리
    @Autowired
    FileUploadRepository rpt;

    /**
     * 업로드 경로 구하기
     * 
     * @param type
     * @return
     */
    public Path getUploadPath(String type) {
        // 파일은 기본적으로 날짜 기준 (yyyymmdd) 으로 폴더를 구분
        LocalDate ld = LocalDate.now();
        String date = ld.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String typeFolder = "";
        // 타입에 따라 날짜 내부에 폴더 구분
        if (type.equals("image")) {
            typeFolder = "image";
        } else if (type.equals("document")) {
            typeFolder = "document";
        } else {
            typeFolder = "";
        }
        // 업로드 경로를 조합
        uploadPath += File.separator + date + File.separator + typeFolder;
        // 조합된 경로 체크
        Path dir = Paths.get(uploadPath);
        // 해당 경로 존재하는지 체크
        if (!Files.exists(dir)) {
            try {
                // 경로가 없다면 생성
                Files.createDirectories(dir);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return dir;
    }

    /**
     * 업로드 하기
     * 
     * @param file
     * @param path
     */
    public HashMap<String, String> upload(MultipartFile file, Path path) {
        HashMap<String, String> result = new HashMap<String, String>();
        // 파일 정보
        String fileName = file.getOriginalFilename();
        String fileSize = Long.toString(file.getSize());
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileType = file.getContentType();
        String filePath = "";
        // 결과 정보
        String status = "";
        String message = "";
        // 예외처리 하기

        // 1. 파일 사이즈
        if (file.getSize() > MAX_SIZE) {
            status = "fail";
            message = "file over max upload size";
            result.put("status", status);
            result.put("message", message);
            return result;
        }

        // 2. 파일 확장자
        // 화이트 리스트 방식으로 파일 확장자 체크
        if (!Arrays.asList("jpg", "png", "gif", "jpeg", "bmp", "xlsx", "ppt", "pptx", "txt", "hwp")
                .contains(fileType.toLowerCase())) {
            status = "fail";
            message = "file type is not allowed";
            result.put("status", status);
            result.put("message", message);
            return result;
        }

        // 3. 저장 파일 이름 랜덤화
        String tempName = fileName.substring(0, fileName.lastIndexOf("."));
        String encFileName = Base64.getEncoder().encodeToString(tempName.getBytes());
        // 암호화된 경로로 패스 설정
        filePath = path.toString() + File.separator + encFileName + "." + fileExt;

        // 4. 파일정보 맵에 담기.
        HashMap<String, String> fileInfo = new HashMap<String, String>();

        fileInfo.put("fileName", fileName);
        fileInfo.put("encFileName", encFileName);
        fileInfo.put("fileSize", fileSize);
        fileInfo.put("fileExt", fileExt);
        fileInfo.put("fileType", fileType);
        fileInfo.put("filePath", filePath);
        
        try {
            InputStream is = file.getInputStream();
            Files.copy(is, path.resolve(encFileName + "." + fileExt), StandardCopyOption.REPLACE_EXISTING);

            // 파일 저장에 성공하면 DB에 저장하기
            int seq = rpt.insertFile(fileInfo);

            status = "success";
            message = "upload complete";
        } catch (Exception e) {
            e.printStackTrace();
            status = "fail";
            message = "upload fail";
        }
        result.put("status", status);
        result.put("message", message);
        return result;
    }
}
```

#### FileUploadService.java

```java
@Override
    public HashMap<String, String> save3(MultipartRequest req) {
        HashMap<String, String> result = new HashMap<String, String>();
        MultipartFile file = req.getFile("singleFile3");
        Path uploadPath = fu.getUploadPath("image");
        result = fu.upload(file, uploadPath);
        return result;
    }
```


```java

	private static String makePath(String originalFilename) {
		var cal = Calendar.getInstance();
		var year = cal.get(Calendar.YEAR);
		var month = cal.get(Calendar.MONTH) + 1;
		var date = cal.get(Calendar.DATE);

		var extension = FilenameUtils.getExtension(originalFilename);
		var fileName = RandomString.make(36) + "." + extension;

		var path = String.format("/%d/%d/%d/%s", year, month, date, fileName);

		return "attachment_file" + path;
	}
```    



```java

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "FILE_M")
@Entity
@NoArgsConstructor
@Getter
public class SyFileM {

    @Id
    @Column(name = "FILE_ID")
    private String fileId;

    @Column(name = "ORGN_FILE_NAME")
    private String orgnFileName;

    @Column(name = "PYSC_FILE_NAME")
    private String pyscFileName;

    @Column(name = "FILE_SIZE")
    private Long fileSize;

s
    public SyFileM(String fileId, String orgnFileName, String pyscFileName, long fileSize) {
        this.fileId       = fileId;
        this.orgnFileName = orgnFileName;
        this.pyscFileName = pyscFileName;
        this.fileSize     = fileSize;
    }
}

```

# FileUploadController.java

```java
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.study.springboot.system.entity.SyFileM;

@Controller
public class FileUploadController {

    private static final Logger logger     = LoggerFactory.getLogger(FileUploadController.class);
    private static final String folderPath = "/tmp/upload/";

    @GetMapping("/upload.do")
    public String upload() {
        return "fileupload";
    }


    @PostMapping(value = "/upload", produces = "application/json; charset=utf8")
    public ResponseEntity<?> uploadFile(@RequestParam("files") MultipartFile[] multiFiles, Model model) {
        Map<String, Object> result = new HashMap<>();

        try {
            createDirIfNotExist();

            SyFileM syFileM = null;
            String  uuId, fileId, orgnFileName, pyscFileName, physicalPath;

            List<SyFileM> fileList = new ArrayList<>();
            for (int i = 0; i < multiFiles.length; i++) {
                if (!multiFiles[i].isEmpty()) {
                    // byte[] bytes = multiFiles[i].getBytes();

                    logger.debug("folderPath :: {}", folderPath);
                    logger.debug("file.getOriginalFilename() :: {}", multiFiles[i].getOriginalFilename());
                    logger.debug("file size :: {}", multiFiles[i].getSize());

                    uuId         = UUID.randomUUID().toString();
                    fileId       = getToDate() + "_" + uuId;
                    orgnFileName = multiFiles[i].getOriginalFilename();
                    pyscFileName = uuId;
                    physicalPath = folderPath + getToDate() + "/";

                    syFileM = new SyFileM(fileId, orgnFileName, pyscFileName, multiFiles[i].getSize());
                    fileList.add(syFileM);

                    // 파일에 저장하기
                    logger.debug("dest :: {}", physicalPath + pyscFileName);
                    File dest = new File(physicalPath + pyscFileName);
                    multiFiles[i].transferTo(dest);
                }
            }

            result.put("success", true);
            result.put("fileList", fileList);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        logger.debug("result :: {}", result);

        return ResponseEntity.ok().body(result);
    }

    private void createDirIfNotExist() {
        // create directory to save the files
        File directory = new File(folderPath + "/" + getToDate());
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    @GetMapping("/files")
    public ResponseEntity<String[]> getListFiles() {
        return ResponseEntity.status(HttpStatus.OK).body(new java.io.File(folderPath).list());
    }

    private String getToDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date             date      = new Date(System.currentTimeMillis());

        return formatter.format(date);
    }
}
```


```java
@PostMapping("/upload")
public ResponseEntity upload(
	@RequestParam("files") MultipartFile[] files // 파일 받기
) {
	  // 1. response 객체 생성
    HashMap<String, Object> resultMap = new HashMap<>();
    HashMap<String, Object> responseData = new HashMap<>();
    
    // 2. 받은 파일 데이터 확인
    List<HashMap<String, Object>> fileNames = new ArrayList<>();
    	Arrays.stream(files).forEach(f -> {
    	HashMap<String, Object> map = new HashMap<>();
	    System.out.println("f.getOriginalFilename() = " + f.getOriginalFilename());
    	map.put("fileName", f.getOriginalFilename());
    	map.put("fileSize", f.getSize());
    
        fileNames.add(map);
    });
    
    // 3. response 하기
    responseData.put("files", fileNames);
    resultMap.put("response", responseData);
    return ResponseEntity.ok().body(resultMap);
}
````
