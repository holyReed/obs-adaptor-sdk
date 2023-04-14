import java.util.Map;

// 获取对象的长度, hash值
public class ObjectMeta {

    // common
    private Long length;

    // aliyun crc64
    private Long crc64;

    // aws md5
    private String md5;

    // origin metadata
    private Map<String, Object> rawMetadata;

    public void setLength(Long length) {
        this.length = length;
    }

    public void setCrc64(Long crc64) {
        this.crc64 = crc64;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setRawMetadata(Map<String, Object> rawMetadata) {
        this.rawMetadata = rawMetadata;
    }
}