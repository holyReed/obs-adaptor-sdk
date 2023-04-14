import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public interface FileOperations {


    /**
     * 获取文件的上传token
     * 默认使用配置中的bucket, 生成临时的AK与SK, 并具有过期时间
     *
     * @param objectKey objectKey
     * @return TokenPo
     */
    TokenPo uploadToken(String objectKey);

    /**
     * 获取文件的上传token,生成临时的AK与SK, 并具有过期时间
     *
     * @param bucketName bucketName
     * @param objectKey  objectKey
     * @return TokenPo
     */
    TokenPo uploadToken(String bucketName, String objectKey);

    /**
     * 获取下载token
     * 默认使用配置中的bucket, 生成临时的AK与SK, 并具有过期时间
     *
     * @param objectKey objectKey
     * @return TokenPo
     */
    TokenPo downloadToken(String objectKey);

    /**
     * 获取下载token, 生成临时的AK与SK, 并具有过期时间
     *
     * @param bucketName bucketName
     * @param objectKey  objectKey
     * @return TokenPo
     */
    TokenPo downloadToken(String bucketName, String objectKey);

    /**
     * 获取文件下载链接
     * 默认使用配置中的bucket, 链接带上临时生成的AK与SK, 并具有过期时间
     *
     * @param objectKey objectKey
     */
    URL getDownloadLink(String objectKey) throws MalformedURLException;

    /**
     * 获取文件下载链接, 链接带上临时生成的AK与SK, 并具有过期时间
     *
     * @param bucketName bucketName
     * @param objectKey  objectKey
     */
    URL getDownloadLink(String bucketName, String objectKey) throws MalformedURLException;


    /**
     * 获取文件下载链接, 链接带上临时生成的AK与SK, 并具有过期时间
     *
     * @param bucketName bucketName
     * @param objectKey  objectKey
     * @param fileName   下载生成的文件名
     */
    URL getDownloadLink(String bucketName, String objectKey, String fileName) throws MalformedURLException;

    /**
     * 获取文件资源链接, 链接带上临时生成的AK与SK, 并具有过期时间
     *
     * @param bucketName bucketName
     * @param objectKey  objectKey
     * @param fileName   下载生成的文件名
     * @param preview    是否预览
     */
    URL getResourceLink(String bucketName, String objectKey, String fileName, boolean preview) throws MalformedURLException;

    /**
     * 获取文件的资源链接
     *
     * @param bucketName bucketName
     * @param objectKey objectKey
     * @param fileName 下载生成的文件名
     * @param preview 是否预览
     * @return
     */
    UrlWrapper getResourceLinkWithToken(String bucketName, String objectKey, String fileName, boolean preview) throws MalformedURLException;

    /**
     * 删除文件
     * 默认使用配置中的bucket
     *
     * @param objectKeys object list
     */
    Boolean deleteFile(List<String> objectKeys);

    /**
     * 删除文件
     *
     * @param bucketName bucketName
     * @param objectKeys object list
     */
    Boolean deleteFile(String bucketName, List<String> objectKeys);

    /**
     * 冻结文件, 转为低频存储, 改变文件的存储类型
     * 使用配置中的bucket
     *
     * @param objectKey objectKey
     */
    Boolean freezeFile(String objectKey);

    /**
     * 解冻文件, 低频存储转热存, 改变文件的存储类型
     * 使用配置中的bucket
     *
     * @param objectKey objectKey
     */
    Boolean unFreezeFile(String objectKey);

    /**
     * 下载不超过50M的文件
     * 使用配置中的bucket
     *
     * @param objectKey objectKey
     */
    File sampleDownloadFile(String objectKey) throws IOException;

    /**
     * 下载不超过50M的文件
     *
     * @param bucketName bucketName
     * @param objectKey  objectKey
     * @return 文件对象
     * @throws IOException
     */
    File sampleDownloadFile(String bucketName, String objectKey) throws IOException;


    /**
     * 简单上传一个字符串内容的文件
     * 使用配置中的bucket
     *
     * @param objectKey objectKey
     * @param content   文件内容
     * @return ObjectMeta
     */
    ObjectMeta sampleUploadFile(String objectKey, String content);

    /**
     * 简单上传一个字符串内容的文件
     *
     * @param bucketName bucketName
     * @param objectKey  objectKey
     * @param content    content
     * @return ObjectMeta
     */
    ObjectMeta sampleUploadFile(String bucketName, String objectKey, String content);

    /**
     * 跨桶拷贝文件, 同步等待结果
     *
     * @param targetRegion
     * @param targetBucket
     * @param targetKey
     * @param srcKey
     * @return
     */
    Boolean copyFile(String targetRegion, String targetBucket, String targetKey, String srcKey);

    /**
     * 获取文件的meta信息, 并构建统一返回对象
     *
     * @param key
     * @return
     */
    ObjectMeta getFileMeta(String key);


    /**
     * 解冻文件, 同步等待结果
     * @param key  文件的key
     * @param days  转储文件保存天数
     * @return
     */
    Boolean restoreFile(String key, int days);

}
