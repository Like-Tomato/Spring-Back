package com.like_lion.tomato.infra.s3.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.like_lion.tomato.infra.s3.dto.request.PresignedUrlReq;
import com.like_lion.tomato.infra.s3.dto.response.PresignedUrlRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * S3 Presigned URL 발급 및 관련 유틸리티 서비스
 */
@Service
@RequiredArgsConstructor
public class S3PresignedService {

    @Value("${cloud.s3.bucket}")
    private String bucket;

    @Value("${cloud.s3.upload.expTime}")
    private Long uploadExpTime;

    @Value("${cloud.s3.download.expTime}")
    private Long downloadExpTime;

    private final AmazonS3 amazonS3;

    /**
     * S3 파일 업로드용 Presigned URL을 생성한다.
     *
     * @param request prefix(폴더 경로), fileName(원본 파일명) 정보를 담은 요청 DTO
     * @return PresignedUrlRes (업로드용 presigned URL, S3에 저장될 파일 key)
     */
    public PresignedUrlRes getPresignedUrlForPut(PresignedUrlReq request) {
        String fileKey = request.fileKey();
        GeneratePresignedUrlRequest presignedRequest = getGeneratePresignedUrlRequest(bucket, fileKey, HttpMethod.PUT);
        URL url = amazonS3.generatePresignedUrl(presignedRequest);
        return PresignedUrlRes.of(url.toString(), fileKey);
    }

    /**
     * S3 파일 다운로드용 Presigned URL을 생성한다.
     *
     * @param fileKey S3에 저장된 파일의 key(경로+파일명)
     * @return PresignedUrlRes (다운로드용 presigned URL, 파일 key)
     */
    public PresignedUrlRes getPresignedUrlForGet(String fileKey) {
        GeneratePresignedUrlRequest presignedRequest = getGeneratePresignedUrlRequest(bucket, fileKey, HttpMethod.GET);
        URL url = amazonS3.generatePresignedUrl(presignedRequest);
        return PresignedUrlRes.of(url.toString(), fileKey);
    }

    public String generateUploadUrl(String fileKey, String contentType, int expirationInSeconds) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += expirationInSeconds * 1000L;
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest presignedRequest = new GeneratePresignedUrlRequest(bucket, fileKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        if (contentType != null && !contentType.isEmpty()) {
            presignedRequest.addRequestParameter("Content-Type", contentType);
        }

        URL url = amazonS3.generatePresignedUrl(presignedRequest);
        return url.toString();
    }

    /**
     * S3 Presigned URL 발급을 위한 요청 객체를 생성한다.
     *
     * @param bucket   S3 버킷 이름
     * @param fileKey  S3에 저장될 파일 key(경로+파일명)
     * @param method   HTTP 메서드 (PUT=업로드, GET=다운로드)
     * @return GeneratePresignedUrlRequest (presigned URL 생성용 요청 객체)
     */
    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String fileKey, HttpMethod method) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        // 업로드/다운로드에 따라 만료 시간 다르게 적용
        expTimeMillis += (method == HttpMethod.PUT ? uploadExpTime : downloadExpTime);
        expiration.setTime(expTimeMillis);

        return new GeneratePresignedUrlRequest(bucket, fileKey)
                .withMethod(method)
                .withExpiration(expiration);
    }

    /**
     * S3에 저장할 파일 key(경로+파일명)를 생성한다.
     * key는 prefix/UUID/파일명 형식으로 구성된다.
     *
     * @param prefix   S3 폴더 경로(예: "session/123")
     * @param fileName 원본 파일명
     * @return S3에 저장할 유니크한 파일 key
     */
    private String createKey(String prefix, String fileName) {
        String fileUniqueId = UUID.randomUUID().toString();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return String.format("%s/%s_%s_%s", prefix, timestamp, fileUniqueId, fileName);
    }
}