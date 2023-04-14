import java.net.URL;

public class UrlWrapper {

    public UrlWrapper(URL url, TokenPo tokenPo) {
        this.url = url;
        this.tokenPo = tokenPo;
    }

    /**
     * 签名的URL
     */
    private URL url;

    /**
     * 签名用到的token对象
     */
    private TokenPo tokenPo;

}
