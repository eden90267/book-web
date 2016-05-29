package my.eden.book.web.logic;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by eden90267 on 2016/5/29.
 */
@Service
public class AWSS3Service {

    @Value("${book.s3.access.key}")
    private String bookS3AccessKey;
    @Value("${book.s3.secret.key}")
    private String bookS3SecretKey;
    @Value("${book.s3.bucket}")
    private String bookS3Bucket;

    public void saveImage(String fileName, MultipartFile image) throws S3ServiceException, IOException {
        AWSCredentials awsCredentials = new AWSCredentials(bookS3AccessKey, bookS3SecretKey);
        S3Service s3 = new RestS3Service(awsCredentials);

        S3Bucket imageBucket = s3.getBucket(bookS3Bucket);
        S3Object imageObject = new S3Object(fileName);
        // 設置圖片資料
        imageObject.setDataInputStream(new ByteArrayInputStream(image.getBytes()));
        imageObject.setContentLength(image.getBytes().length);
        imageObject.setContentType("image/jpeg");
        // 設置權限
        AccessControlList acl = new AccessControlList();
        acl.setOwner(imageBucket.getOwner());
        acl.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);

        imageObject.setAcl(acl);

        s3.putObject(imageBucket, imageObject);
    }

}
