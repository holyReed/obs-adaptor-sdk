public class TokenPo {

    /**
     * sts 访问账号
     */
    private String stsAccessKeyId;

    /**
     * sts 访问秘钥
     */
    private String stsAccessKeySecret;

    /**
     * sts 访问token
     */
    private String stsToken;

    /**
     * 过期时间 毫秒
     */
    private long stsExpirationMillisSec;

    public String getStsAccessKeyId() {
        return stsAccessKeyId;
    }

    public void setStsAccessKeyId(String stsAccessKeyId) {
        this.stsAccessKeyId = stsAccessKeyId;
    }

    public String getStsAccessKeySecret() {
        return stsAccessKeySecret;
    }

    public void setStsAccessKeySecret(String stsAccessKeySecret) {
        this.stsAccessKeySecret = stsAccessKeySecret;
    }

    public String getStsToken() {
        return stsToken;
    }

    public void setStsToken(String stsToken) {
        this.stsToken = stsToken;
    }

    public long getStsExpirationMillisSec() {
        return stsExpirationMillisSec;
    }

    public void setStsExpirationMillisSec(long stsExpirationMillisSec) {
        this.stsExpirationMillisSec = stsExpirationMillisSec;
    }

    @Override
    public String toString() {
        return "TokenPo{" +
                "stsAccessKeyId='" + stsAccessKeyId + '\'' +
                ", stsAccessKeySecret='" + stsAccessKeySecret + '\'' +
                ", stsToken='" + stsToken + '\'' +
                ", stsExpirationMillisSec=" + stsExpirationMillisSec +
                '}';
    }
}