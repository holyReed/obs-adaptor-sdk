import com.huaweicloud.sdk.core.Constants;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObsFileOperations implements FileOperations{

    private static final Logger LOGGER = LoggerFactory.getLogger(ObsFileOperations.class);

    private StorageProperties storageProperties;

    public ObsFileOperations(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    public StorageProperties getStorageProperties() {
        return storageProperties;
    }

    @Override
    public TokenPo uploadToken(String objectKey) {
        return FileOperationsUtils.getTemporalAKSK();
    }

    @Override
    public TokenPo uploadToken(String bucketName, String objectKey) {
        return FileOperationsUtils.getTemporalAKSK();
    }

    @Override
    public TokenPo downloadToken(String objectKey) {
        return FileOperationsUtils.getTemporalAKSK();
    }

    @Override
    public TokenPo downloadToken(String bucketName, String objectKey) {
        return FileOperationsUtils.getTemporalAKSK();
    }

    public URL getDownloadLink(String objectKey) throws MalformedURLException {
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, FileOperationsUtils.getExpireSeconds());
        TemporarySignatureResponse response = FileOperationsUtils.createTemporarySignature("",objectKey);
        return new URL(response.getSignedUrl());
    }

    public URL getDownloadLink(String bucketName, String objectKey) throws MalformedURLException {
        TemporarySignatureResponse response = FileOperationsUtils.createTemporarySignature(bucketName,objectKey);
        return new URL(response.getSignedUrl());
    }

    // 无需 fileName 参数
    public URL getDownloadLink(String bucketName, String objectKey, String fileName) throws MalformedURLException {
        TemporarySignatureResponse response = FileOperationsUtils.createTemporarySignature(bucketName,objectKey);
        return new URL(response.getSignedUrl());
    }

    @Override
    public URL getResourceLink(String bucketName, String objectKey, String fileName, boolean preview) throws MalformedURLException {
        System.out.println("getResourceLink(String bucketName, String objectKey, String fileName, boolean preview)");
        ObsClient obsClient = new ObsClient(FileOperationsUtils.getStorageProperties().getAccesskeyId(), FileOperationsUtils.getStorageProperties().getAccesskeySecret(), FileOperationsUtils.getStorageProperties().getEndpoint());
        // 通过修改元数据的ContentDisposition获取文件预览或下载的URL：inline预览、attachment下载
        String contentDisposition = preview ? "inline" : "attachment";

        SetObjectMetadataRequest metadataRequest = new SetObjectMetadataRequest(bucketName, objectKey);
        metadataRequest.setContentDisposition(contentDisposition);

        ObjectMetadata metadata = obsClient.setObjectMetadata(metadataRequest);
        System.out.println("ContentDisposition = " + metadata.getContentDisposition());

        TemporarySignatureRequest temporarySignatureRequest = new TemporarySignatureRequest(HttpMethodEnum.GET, FileOperationsUtils.getExpireSeconds());
        temporarySignatureRequest.setBucketName(bucketName);
        temporarySignatureRequest.setObjectKey(objectKey);

        TemporarySignatureResponse response = obsClient.createTemporarySignature(temporarySignatureRequest);

        System.out.println("Getting object using temporary signature url:");
        System.out.println("\t" + response.getSignedUrl());
        return new URL(response.getSignedUrl());
    }

    @Override
    public UrlWrapper getResourceLinkWithToken(String bucketName, String objectKey, String fileName, boolean preview) throws MalformedURLException {
        System.out.println("getDownloadLink(String bucketName, String objectKey, String fileName)");
        ObsClient obsClient = new ObsClient(FileOperationsUtils.getStorageProperties().getAccesskeyId(), FileOperationsUtils.getStorageProperties().getAccesskeySecret(), FileOperationsUtils.getStorageProperties().getEndpoint());
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, FileOperationsUtils.getExpireSeconds());
        request.setBucketName(bucketName);
        request.setObjectKey(objectKey);
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);

        System.out.println("Getting object using temporary signature url:");
        System.out.println("\t" + response.getSignedUrl());

        URL url = new URL(response.getSignedUrl());
        TokenPo tokenPo = new TokenPo();

        return new UrlWrapper(url, tokenPo);
    }

    @Override
    public Boolean deleteFile(List<String> objectKeys) {
        return FileOperationsUtils.deleteObject(null,objectKeys);
    }

    @Override
    public Boolean deleteFile(String bucketName, List<String> objectKeys) {
        return FileOperationsUtils.deleteObject(bucketName,objectKeys);
    }

    public Boolean freezeFile(String objectKey) {
        ObsClient obsClient = new ObsClient(FileOperationsUtils.getStorageProperties().getAccesskeyId(), FileOperationsUtils.getStorageProperties().getAccesskeySecret(), FileOperationsUtils.getStorageProperties().getEndpoint());
        SetObjectMetadataRequest request = new SetObjectMetadataRequest(storageProperties.getBucket(), objectKey);
        request.setObjectStorageClass(StorageClassEnum.WARM);
        HeaderResponse response = obsClient.setObjectMetadata(request);
        System.out.println("freezeFile: " + obsClient.getObjectMetadata(storageProperties.getBucket(),objectKey));
        return response.getStatusCode() == Constants.StatusCode.SUCCESS;
    }

    public Boolean unFreezeFile(String objectKey) {
        ObsClient obsClient = new ObsClient(FileOperationsUtils.getStorageProperties().getAccesskeyId(), FileOperationsUtils.getStorageProperties().getAccesskeySecret(), FileOperationsUtils.getStorageProperties().getEndpoint());
        SetObjectMetadataRequest request = new SetObjectMetadataRequest(storageProperties.getBucket(), objectKey);
        request.setObjectStorageClass(StorageClassEnum.STANDARD);
        HeaderResponse response = obsClient.setObjectMetadata(request);
        System.out.println("unFreezeFile : " +  obsClient.getObjectMetadata(storageProperties.getBucket(),objectKey));
        return response.getStatusCode() == Constants.StatusCode.SUCCESS;
    }

    public File sampleDownloadFile(String objectKey) throws IOException {

        return sampleDownloadFile(storageProperties.getBucket(), objectKey);
    }

    public File sampleDownloadFile(String bucketName, String objectKey) throws IOException {
        ObsClient obsClient = new ObsClient(FileOperationsUtils.getStorageProperties().getAccesskeyId(), FileOperationsUtils.getStorageProperties().getAccesskeySecret(), FileOperationsUtils.getStorageProperties().getEndpoint());
        ObsObject obsObject = obsClient.getObject(bucketName, objectKey);
        InputStream content = obsObject.getObjectContent();

        File file = new File("C:\\yangyin\\project\\gene-an\\" + objectKey);
        if (content != null) {
            if (content.available() > 1024 * 1024 * 50L) {
                System.out.println("下载文件超过50M");
                throw new IOException();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            FileWriter fileWriter = new FileWriter(file);
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                fileWriter.write(line);
                System.out.println("\n" + line);
            }
            reader.close();
            fileWriter.close();
        }
        return file;
    }

    public ObjectMeta sampleUploadFile(String objectKey, String content) {
        //没有bucket , OBS上传需要指定bucket名称
        return sampleUploadFile(storageProperties.getBucket(), objectKey, content);
    }

    public ObjectMeta sampleUploadFile(String bucketName, String objectKey, String content) {
        ObsClient obsClient = new ObsClient(FileOperationsUtils.getStorageProperties().getAccesskeyId(), FileOperationsUtils.getStorageProperties().getAccesskeySecret(), FileOperationsUtils.getStorageProperties().getEndpoint());
        PutObjectResult result = obsClient.putObject(bucketName, objectKey, new ByteArrayInputStream(content.getBytes()));
        ObjectMeta objectMeta = new ObjectMeta();
        if (result.getStatusCode() == Constants.StatusCode.SUCCESS) {
            ObjectMetadata obsMeta = obsClient.getObject(bucketName, objectKey).getMetadata();
            objectMeta.setLength(obsMeta.getContentLength());
            objectMeta.setMd5(obsMeta.getContentMd5());
            objectMeta.setRawMetadata(obsMeta.getAllMetadata());
        }
        return objectMeta;
    }

    // 华为云OBS暂不支持跨区域复制
    @Override
    public Boolean copyFile(String targetRegion, String targetBucket, String targetKey, String srcKey) {
        TokenPo tokenPo = FileOperationsUtils.getTemporalAKSK();
        if (StringUtils.isEmpty(tokenPo.getStsAccessKeyId()) || StringUtils.isEmpty(tokenPo.getStsAccessKeySecret())) {
            LOGGER.error("get temporalAKSK fail.");
            return false;
        }

        String endpoint = String.format("https://obs.%s.myhuaweicloud.com", targetRegion);
        // 创建ObsClient实例
        final ObsClient obsClient = new ObsClient(tokenPo.getStsAccessKeyId(), tokenPo.getStsAccessKeySecret(),
                tokenPo.getStsToken(), endpoint);
        String sourceBucketName = getStorageProperties().getBucket();

        // 初始化线程池，线程池大小根据配置修改，StorageProperties.corePoolSize
        ExecutorService executorService = Executors.newFixedThreadPool(getStorageProperties().getCorePoolSize());

        // 初始化分段上传任务
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(targetBucket, targetKey);
        InitiateMultipartUploadResult result = obsClient.initiateMultipartUpload(request);

        final String uploadId = result.getUploadId();
        LOGGER.info("uploadId is {}.", uploadId);

        // 获取大对象信息
        ObjectMetadata metadata = obsClient.getObjectMetadata(sourceBucketName, srcKey);
        // 每段复制150MB，StorageProperties.partSize
        long partSize = getStorageProperties().getPartSize();
        long objectSize = metadata.getContentLength();

        // 计算需要复制的段数
        long partCount = objectSize % partSize == 0 ? objectSize / partSize : objectSize / partSize + 1;

        final List<PartEtag> partEtags = Collections.synchronizedList(new ArrayList<PartEtag>());

        // 执行并发复制段
        for (int i = 0; i < partCount; i++)
        {
            // 复制段起始位置
            final long rangeStart = i * partSize;
            // 复制段结束位置
            final long rangeEnd = (i + 1 == partCount) ? objectSize - 1 : rangeStart + partSize - 1;
            // 分段号
            final int partNumber = i + 1;
            executorService.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    CopyPartRequest request = new CopyPartRequest();
                    request.setUploadId(uploadId);
                    request.setSourceBucketName(sourceBucketName);
                    request.setSourceObjectKey(srcKey);
                    request.setDestinationBucketName(targetBucket);
                    request.setDestinationObjectKey(targetKey);
                    request.setByteRangeStart(rangeStart);
                    request.setByteRangeEnd(rangeEnd);
                    request.setPartNumber(partNumber);
                    CopyPartResult result;
                    try
                    {
                        result = obsClient.copyPart(request);
                        System.out.println("Part#" + partNumber + " done\n");
                        partEtags.add(new PartEtag(result.getEtag(), result.getPartNumber()));
                    }
                    catch (ObsException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        // 等待复制完成
        executorService.shutdown();
        while (!executorService.isTerminated())
        {
            try
            {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        // 合并段
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(targetBucket,
                targetKey, uploadId, partEtags);
        CompleteMultipartUploadResult uploadResult = obsClient.completeMultipartUpload(completeMultipartUploadRequest);


        return uploadResult != null && uploadResult.getStatusCode() == 200;
    }

    @Override
    public ObjectMeta getFileMeta(String key) {
        TokenPo tokenPo = FileOperationsUtils.getTemporalAKSK();
        if (StringUtils.isEmpty(tokenPo.getStsAccessKeyId()) || StringUtils.isEmpty(tokenPo.getStsAccessKeySecret())) {
            LOGGER.error("get temporalAKSK fail");
            return null;
        }

        try(ObsClient obsClient = new ObsClient(tokenPo.getStsAccessKeyId(),tokenPo.getStsAccessKeySecret(),
                tokenPo.getStsToken(), getStorageProperties().getPublicEndpoint())) {
            ObjectMetadata objectMetadata = obsClient.getObjectMetadata(getStorageProperties().getBucket(), key);
            if (null == objectMetadata) {
                LOGGER.error("get {} metadata fail.", key);
                return null;
            }

            ObjectMeta result = new ObjectMeta();
            result.setLength(objectMetadata.getContentLength());
            result.setCrc64(Long.getLong(objectMetadata.getCrc64()));
            result.setMd5(objectMetadata.getContentMd5());
            result.setRawMetadata(objectMetadata.getOriginalHeaders());
            return result;
        } catch (Exception e) {
            LOGGER.error("get {} metadata exception happen.", key, e);
        }
        return null;
    }

    @Override
    public Boolean restoreFile(String key, int days) {
        TokenPo tokenPo = FileOperationsUtils.getTemporalAKSK();
        if (StringUtils.isEmpty(tokenPo.getStsAccessKeyId()) || StringUtils.isEmpty(tokenPo.getStsAccessKeySecret())) {
            LOGGER.error("get temporalAKSK fail");
            return false;
        }

        try (ObsClient obsClient = new ObsClient(tokenPo.getStsAccessKeyId(), tokenPo.getStsAccessKeySecret(),
                tokenPo.getStsToken(), getStorageProperties().getPublicEndpoint())) {
            RestoreObjectRequest request = new RestoreObjectRequest(getStorageProperties().getBucket(), key, days);
            // 使用快速恢复方式，恢复对象
            request.setRestoreTier(RestoreTierEnum.EXPEDITED);
            RestoreObjectRequest.RestoreObjectStatus status = obsClient.restoreObject(request);

            return null != status && status.getCode() == 202;
        } catch (Exception e) {
            LOGGER.error("restore {} file exception happen.", key, e);
        }
        return false;
    }
}
