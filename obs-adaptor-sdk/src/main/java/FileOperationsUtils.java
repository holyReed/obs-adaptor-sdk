import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.*;
import com.huaweicloud.sdk.iam.v3.region.IamRegion;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileOperationsUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileOperationsUtils.class);

    private static StorageProperties storageProperties = new StorageProperties();

    private static String obsEndPoint = "https://obs.cn-east-2.myhuaweicloud.com/";

    private static String userName = "AECD051698";

    private static String password = "Clo***";

    private static String ak = "SKKLWOZ***";

    private static String sk = "wBF8GKZ4ZCN***";

    private static String region = "cn-east-2";

    private static String defaultBucketName = "my-obs-bucket-demo";

    private static Integer expireSeconds = 3600;

    private static String token;

    public static long getExpireSeconds() {
        return expireSeconds;
    }

    public static String getToken() {
        return token;
    }

    public static StorageProperties getStorageProperties() {
        return storageProperties;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setToken(String accessToken) {
        token = accessToken;
    }

    public static void setExpireSeconds(Integer expireSeconds) {
        FileOperationsUtils.expireSeconds = expireSeconds;
    }

    public static void setUserName(String userName) {
        FileOperationsUtils.userName = userName;
    }

    public static void setStorageProperties(StorageProperties customStorageProperties) {
        System.out.println("init storageProperties.");
        if (StringUtils.isBlank(customStorageProperties.getAccesskeyId())) {
            storageProperties.setAccesskeyId(ak);
        }else {
            storageProperties.setAccesskeyId(customStorageProperties.getAccesskeyId());
        }

        if (StringUtils.isBlank(customStorageProperties.getAccesskeySecret())) {
            storageProperties.setAccesskeySecret(sk);
        }else {
            storageProperties.setAccesskeySecret(customStorageProperties.getAccesskeySecret());
        }

        if (StringUtils.isBlank(customStorageProperties.getAccountId())) {
            storageProperties.setAccesskeyId(userName);
        }else {
            storageProperties.setAccesskeyId(customStorageProperties.getAccountId());
        }

        if (StringUtils.isBlank(customStorageProperties.getRegion())) {
            storageProperties.setRegion(region);
        }else {
            storageProperties.setRegion(customStorageProperties.getRegion());
        }

        if (StringUtils.isBlank(customStorageProperties.getEndpoint())) {
            storageProperties.setEndpoint(obsEndPoint);
        }else {
            storageProperties.setEndpoint(customStorageProperties.getEndpoint());
        }

        if (StringUtils.isBlank(customStorageProperties.getPublicEndpoint())) {
            storageProperties.setPublicEndpoint(obsEndPoint);
        }else {
            storageProperties.setPublicEndpoint(customStorageProperties.getPublicEndpoint());
        }

        if (StringUtils.isBlank(String.valueOf(customStorageProperties.getDownloadExpireTimeSec()))) {
            storageProperties.setDownloadExpireTimeSec(expireSeconds);
        }else {
            storageProperties.setDownloadExpireTimeSec(customStorageProperties.getDownloadExpireTimeSec());
        }

        if (StringUtils.isBlank(String.valueOf(customStorageProperties.getTokenExpireTimeSec()))) {
            storageProperties.setTokenExpireTimeSec(expireSeconds);
        }else {
            storageProperties.setTokenExpireTimeSec(customStorageProperties.getTokenExpireTimeSec());
        }
    }

    public static String getUserToken(StorageProperties storageProperties) {

        if (StringUtils.isNotEmpty(storageProperties.getAccesskeyId()) && StringUtils.isNotEmpty(storageProperties.getAccesskeySecret()) && StringUtils.isNotEmpty(storageProperties.getAccountId())) {
            ak = storageProperties.getAccesskeyId();
            sk = storageProperties.getAccesskeySecret();
            userName = storageProperties.getAccountId();
        }

        ICredential auth = new GlobalCredentials()
                .withAk(FileOperationsUtils.getStorageProperties().getAccesskeyId())
                .withSk(FileOperationsUtils.getStorageProperties().getAccesskeySecret());

        IamClient client = IamClient.newBuilder()
                .withCredential(auth)
                .withRegion(IamRegion.valueOf(FileOperationsUtils.getStorageProperties().getRegion()))
                .build();
        KeystoneCreateUserTokenByPasswordRequest request = new KeystoneCreateUserTokenByPasswordRequest();
        KeystoneCreateUserTokenByPasswordRequestBody body = new KeystoneCreateUserTokenByPasswordRequestBody();
        PwdPasswordUserDomain domainUser = new PwdPasswordUserDomain();
        domainUser.withName(userName);
        PwdPasswordUser userPassword = new PwdPasswordUser();
        userPassword.withDomain(domainUser)
                .withName(userName)
                .withPassword(password);
        PwdPassword passwordIdentity = new PwdPassword();
        passwordIdentity.withUser(userPassword);
        List<PwdIdentity.MethodsEnum> listIdentityMethods = new ArrayList<>();
        listIdentityMethods.add(PwdIdentity.MethodsEnum.fromValue("password"));
        PwdIdentity identityAuth = new PwdIdentity();
        identityAuth.withMethods(listIdentityMethods)
                .withPassword(passwordIdentity);
        PwdAuth authbody = new PwdAuth();
        authbody.withIdentity(identityAuth);
        body.withAuth(authbody);
        request.withBody(body);
        try {
            KeystoneCreateUserTokenByPasswordResponse response = client.keystoneCreateUserTokenByPassword(request);
            System.out.println(response.getXSubjectToken());
            FileOperationsUtils.setToken(response.getXSubjectToken());
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            e.printStackTrace();
            System.out.println(e.getHttpStatusCode());
            System.out.println(e.getRequestId());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorMsg());
        }
        return FileOperationsUtils.getToken();
    }

    public static TokenPo getTemporalAKSK() {
        TokenPo tokenPo = new TokenPo();

        ICredential auth = new GlobalCredentials()
                .withAk(FileOperationsUtils.getStorageProperties().getAccesskeyId())
                .withSk(FileOperationsUtils.getStorageProperties().getAccesskeySecret());

        IamClient client = IamClient.newBuilder()
                .withCredential(auth)
                .withRegion(IamRegion.valueOf(FileOperationsUtils.getStorageProperties().getRegion()))
                .build();
        CreateTemporaryAccessKeyByTokenRequest request = new CreateTemporaryAccessKeyByTokenRequest();
        CreateTemporaryAccessKeyByTokenRequestBody body = new CreateTemporaryAccessKeyByTokenRequestBody();
        List<TokenAuthIdentity.MethodsEnum> listIdentityMethods = new ArrayList<>();
        listIdentityMethods.add(TokenAuthIdentity.MethodsEnum.fromValue("token"));
        TokenAuthIdentity identityAuth = new TokenAuthIdentity();
        identityAuth.withMethods(listIdentityMethods);
        TokenAuth authbody = new TokenAuth();
        authbody.withIdentity(identityAuth);
        body.withAuth(authbody);
        request.withBody(body);
        try {
            CreateTemporaryAccessKeyByTokenResponse response = client.createTemporaryAccessKeyByToken(request);
            System.out.println(response.getCredential());
            tokenPo.setStsAccessKeyId(response.getCredential().getAccess());
            tokenPo.setStsAccessKeySecret(response.getCredential().getSecret());
            tokenPo.setStsToken(response.getCredential().getSecuritytoken());
            System.out.println(tokenPo);
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            e.printStackTrace();
            System.out.println(e.getHttpStatusCode());
            System.out.println(e.getRequestId());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorMsg());
        }

        return tokenPo;
    }

    public static Boolean deleteObject(String bucket, List<String> objectKeys) {

        String obsBucket;
        ObsClient obsClient = null;
        obsBucket = StringUtils.isBlank(bucket) ? defaultBucketName : bucket;
        String endpoint = obsEndPoint + obsBucket;

        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endpoint);
        try {
            obsClient = new ObsClient(FileOperationsUtils.getStorageProperties().getAccesskeyId(), FileOperationsUtils.getStorageProperties().getAccesskeySecret(), config);

            System.out.println("\nDeleting all objects\n");
            DeleteObjectsRequest request = new DeleteObjectsRequest();
            request.setBucketName(obsBucket);
            request.setQuiet(false);

            KeyAndVersion[] kvs = new KeyAndVersion[objectKeys.size()];
            int index = 0;
            for (String key : objectKeys) {
                kvs[index++] = new KeyAndVersion(key);
            }

            request.setKeyAndVersions(kvs);
            System.out.println("Delete results:");
            DeleteObjectsResult deleteObjectsResult = obsClient.deleteObjects(request);

            for (DeleteObjectsResult.DeleteObjectResult object : deleteObjectsResult.getDeletedObjectResults()) {
                System.out.println("\t" + object);
            }

            System.out.println("Error results:");

            for (DeleteObjectsResult.ErrorResult error : deleteObjectsResult.getErrorResults()) {
                System.out.println("\t" + error);
            }

            System.out.println();
        } catch (ObsException e) {
            System.out.println("Response Code: " + e.getResponseCode());
            System.out.println("Error Message: " + e.getErrorMessage());
            System.out.println("Error Code:       " + e.getErrorCode());
            System.out.println("Request ID:      " + e.getErrorRequestId());
            System.out.println("Host ID:           " + e.getErrorHostId());
        } finally {
            if (obsClient != null) {
                try {
                    /*
                     * Close obs client
                     */
                    obsClient.close();
                } catch (IOException e) {
                }
            }
        }
        return true;
    }

    public static TemporarySignatureResponse createTemporarySignature(String bucketName, String objectKey) {
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
        request.setBucketName(StringUtils.isBlank(bucketName) ? defaultBucketName : bucketName);
        request.setObjectKey(objectKey);

        ObsClient obsClient = new ObsClient(FileOperationsUtils.getStorageProperties().getAccesskeyId(), FileOperationsUtils.getStorageProperties().getAccesskeySecret(), FileOperationsUtils.getStorageProperties().getEndpoint());
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        System.out.println("Getting object using temporary signature url:");
        System.out.println("\t" + response.getSignedUrl());
        return response;
    }

    public static void main(String[] args) {
        LOGGER.info("test log auth token!");
        System.out.println("test auth token!");
    }
}