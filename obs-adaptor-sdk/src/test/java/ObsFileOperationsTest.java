import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

class ObsFileOperationsTest {

    private ObsFileOperations obsFileOperations = null;
    private StorageProperties storageProperties = null;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        storageProperties = new StorageProperties();
        obsFileOperations = new ObsFileOperations(storageProperties);
    }

    StorageProperties getProperties() {
        StorageProperties properties = new StorageProperties();
        properties.setBucket("obs-python-test-h");
        properties.setPublicEndpoint("https://obs.cn-east-2.myhuaweicloud.com");

        return properties;
    }

    ObsFileOperations getOperations() {
        ObsFileOperations operations = new ObsFileOperations(getProperties());
        return operations;
    }

    /**
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }
**/
    @org.junit.jupiter.api.Test
    void uploadToken() {

        System.out.println("start test upload token");
        FileOperationsUtils.getUserToken(storageProperties);
        System.out.println("end upload token");

    }

    @org.junit.jupiter.api.Test
    void testUploadToken() {
    }

    @org.junit.jupiter.api.Test
    void downloadToken() {
    }

    @org.junit.jupiter.api.Test
    void testDownloadToken() {
    }

    @Test
    void getDownloadLink() throws MalformedURLException {
        obsFileOperations.getDownloadLink("test123.txt");
    }

    @org.junit.jupiter.api.Test
    void testGetDownloadLink() throws MalformedURLException {
        obsFileOperations.getDownloadLink("obs-fjx",  "test2.txt");
    }

    @org.junit.jupiter.api.Test
    void testGetDownloadLink1() throws MalformedURLException {
        obsFileOperations.getDownloadLink("obs-fjx",  "test2.txt", "D:\\DownLoads\\Chrome\\b.txt");
    }

    @org.junit.jupiter.api.Test
    void getResourceLink() {
//        // OBS不支持预览
//        huaweiFileOperations.getResourceLink("obs-fjx", "test2.txt", "D:\\DownLoads\\Chrome\\b.txt", false);
    }

    @org.junit.jupiter.api.Test
    void getResourceLinkWithToken() {
//        // 需确认客户对 文件的资源链接 的定义和诉求
//        huaweiFileOperations.getResourceLinkWithToken("obs-fjx", "test2.txt", "D:\\DownLoads\\Chrome\\b.txt", false);
    }

    @org.junit.jupiter.api.Test
    void deleteFile() {
    }

    @org.junit.jupiter.api.Test
    void testDeleteFile() {
    }

    @org.junit.jupiter.api.Test
    void freezeFile() {
    }

    @org.junit.jupiter.api.Test
    void unFreezeFile() {
    }

    @org.junit.jupiter.api.Test
    void sampleDownloadFile() {
    }

    @org.junit.jupiter.api.Test
    void testSampleDownloadFile() {
    }

    @org.junit.jupiter.api.Test
    void sampleUploadFile() {
    }

    @org.junit.jupiter.api.Test
    void testSampleUploadFile() {
    }

    @org.junit.jupiter.api.Test
    void copyFile() {
        getOperations().copyFile("cn-east-2", "obs-myk", "mayikaitest2.txt", "mayikaitest2.txt");
    }

    @org.junit.jupiter.api.Test
    void getFileMeta() {
        getOperations().getFileMeta("mayikaitest2.txt");
    }

    @org.junit.jupiter.api.Test
    void restoreFile() {
        getOperations().restoreFile("mayikaitest.txt", 1);
    }
}