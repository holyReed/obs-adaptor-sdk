/**
 * 不同存储介质的配置文件
 **/
public class StorageProperties {


    /**
     * 存储类型, oss/s3
     */
    private String type;

    /**
     * 子账户id
     */
    private String accesskeyId;

    /**
     * 子账号秘钥
     */
    private String accesskeySecret;

    /**
     * 系统储存桶
     */
    private String bucket;

    /**
     * s3操作用户的资源名称, s3必须
     */
    private String roleArn;

    /**
     * 账号ID, s3必须
     */
    private String accountId;

    /**
     * 系统日志bucket
     */
    private String logBucket;

    /* 上面是必填,下面为选填 */

    /**
     * oss请求节点,默认:cn-shezhen
     */
    private String endpoint ;

    /**
     * 公网访问的endpoint
     */
    private String publicEndpoint;
    /**
     * s3区域, 默认:us-east-1
     */
    private String region;

    /**
     * 预览链接过期时间(单位秒), 默认300
     */
    private Integer downloadExpireTimeSec = 300;

    /**
     * token过期时间(单位秒), 默认43200
     */
    private Integer tokenExpireTimeSec = 43200;

    /**
     * 使用sts授权时的session名称
     */
    private String roleSessionName = "sessionName";

    /**
     * 拷贝文件的的线程池大小, 默认10
     */
    private Integer corePoolSize = 10;

    /**
     * 设置分片大小为150MB。(经过多次测试150m是时间较小的拆分大小)
     */
    private Long partSize = 1024 * 1024 * 150L;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccesskeyId() {
        return accesskeyId;
    }

    public void setAccesskeyId(String accesskeyId) {
        this.accesskeyId = accesskeyId;
    }

    public String getAccesskeySecret() {
        return accesskeySecret;
    }

    public void setAccesskeySecret(String accesskeySecret) {
        this.accesskeySecret = accesskeySecret;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getRoleArn() {
        return roleArn;
    }

    public void setRoleArn(String roleArn) {
        this.roleArn = roleArn;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getLogBucket() {
        return logBucket;
    }

    public void setLogBucket(String logBucket) {
        this.logBucket = logBucket;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getPublicEndpoint() {
        return publicEndpoint;
    }

    public void setPublicEndpoint(String publicEndpoint) {
        this.publicEndpoint = publicEndpoint;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getDownloadExpireTimeSec() {
        return downloadExpireTimeSec;
    }

    public void setDownloadExpireTimeSec(Integer downloadExpireTimeSec) {
        this.downloadExpireTimeSec = downloadExpireTimeSec;
    }

    public Integer getTokenExpireTimeSec() {
        return tokenExpireTimeSec;
    }

    public void setTokenExpireTimeSec(Integer tokenExpireTimeSec) {
        this.tokenExpireTimeSec = tokenExpireTimeSec;
    }

    public String getRoleSessionName() {
        return roleSessionName;
    }

    public void setRoleSessionName(String roleSessionName) {
        this.roleSessionName = roleSessionName;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Long getPartSize() {
        return partSize;
    }

    public void setPartSize(Long partSize) {
        this.partSize = partSize;
    }
}
