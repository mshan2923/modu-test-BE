package com.example.modu.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "S3Config")
public class S3Config {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3 amazonS3;

    //****** 기존에 있던 이미지 제거기능이 없어요!

    public boolean uploadChecker(MultipartFile multipartFile) throws IOException
    {
        if (multipartFile.isEmpty())
            //return ResponseEntity.ok("업로드 하지 않음");
            return false;
        else if (multipartFile.getSize() > 0)
        {
            throw new IOException("파일이 유효 하지 않음");
        } else
            //return ResponseEntity.ok("업로드 확인 , 저장은 안함");
            return true;
    }
    public String upload(MultipartFile multipartFile) throws IOException {

        if (multipartFile == null)
            return "";
        if (multipartFile.isEmpty())
            return "";

        if (multipartFile.getSize() > 13107200)
            throw new FileSizeLimitExceededException("파일 크기가 100MB가 넘습니다.", multipartFile.getSize(), 13107200);

        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        // 파일 중복 방지를 위해 렌덤값 추가 / 키값

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());
        // Spring Server에서 S3로 파일을 업로드해야 하는데,
        // 이 때 파일의 사이즈를 ContentLength로 S3에 알려주기 위해서 ObjectMetadata를 사용합니다.

        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
        //putObject를 이용하여 파일 Stream을 열어서 S3에 파일을 업로드 합니다.

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }
    public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" +  originalFilename + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);

    }
    public ResponseEntity<byte[]> download(@PathVariable String fileName) throws IOException {

        return ResponseEntity.ok(amazonS3.getObject(bucket, fileName).getObjectContent().readAllBytes());
    }
    public String GetImage(String filename)
    {
        return amazonS3.getUrl(bucket, filename).toString();
    }
    public void deleteImage(String originalFilename)
    {
        amazonS3.deleteObject(bucket, originalFilename);
    }
}