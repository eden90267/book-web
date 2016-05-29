package my.eden.book.web.controller;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import my.eden.book.web.exception.ImageUploadException;
import my.eden.book.web.logic.BookService;
import my.eden.book.web.rest.client.vo.Book;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eden_liu on 2016/5/27.
 */
@Slf4j
@Controller
@RequestMapping(value = {"", "/book"})
public class BookController {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");

    @Autowired
    private BookService service;

    @Value("${book.s3.access.key}")
    private String bookS3AccessKey;
    @Value("${book.s3.secret.key}")
    private String bookS3SecretKey;
    @Value("${book.s3.bucket}")
    private String bookS3Bucket;

    @RequestMapping(value = {"/", "index"})
    public String index(Model model) {
        model.addAttribute("books", service.findAll());
        return "book/index";
    }

    @RequestMapping(value = "create")
    public String create(Model model) {
        model.addAttribute("book", Book.builder().build());
        return "book/create";
    }

    @RequestMapping(value = "validBookNameIsExists")
    @ResponseBody
    public String validBookNameIsExists(@RequestParam String bookName) {
        return String.valueOf(Strings.isNullOrEmpty(bookName) || !service.hasDataByBookName(bookName));
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String save(@Valid Book book,
                       BindingResult bindingResult,
                       @RequestParam(value = "image", required = false) MultipartFile image) {
        if (bindingResult.hasErrors()) {
            return "book/create";
        }
        if (!Strings.isNullOrEmpty(book.getBookName()) && service.hasDataByBookName(book.getBookName())) {
            bindingResult.rejectValue("bookName", "Such bookName already exists", "Such bookName already exists");
            return "book/create";
        }
        book = service.save(book);
        try {
            if (!image.isEmpty()) {
                validateImage(image);
                String bookImageName = book.getId() + "-" + sdf.format(new Date()) + ".jpeg";
                saveImage(bookImageName, image);
                book.setBookImage(bookImageName);
                service.update(book);
            }
        } catch (ImageUploadException | S3ServiceException | IOException ex) {
            service.delete(book); // rollback
            log.error(ex.getMessage(), ex);
            bindingResult.rejectValue("bookImage", ex.getMessage(), ex.getMessage());
            return "book/create";
        }
        return "redirect:/book/index";
    }

    @RequestMapping(value = "edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", service.findOne(id));
        return "book/edit";
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
    public String update(@PathVariable("id") Long id,
                         @Valid Book book,
                         BindingResult bindingResult,
                         @RequestParam(value = "image", required = false) MultipartFile image) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        try {
            if (image != null && !image.isEmpty()) {
                validateImage(image);
                String bookImageName = book.getId() + "-" + sdf.format(new Date()) + ".jpeg";
                saveImage(bookImageName, image);
                book.setBookImage(bookImageName);
            }
        } catch (ImageUploadException | S3ServiceException | IOException ex) {
            log.error(ex.getMessage(), ex);
            bindingResult.rejectValue("bookImage", ex.getMessage(), ex.getMessage());
            return "book/edit";
        }
        service.update(book);
        return "redirect:/book/index";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id) {
        service.delete(service.findOne(id));
        return "book/index";
    }

    @RequestMapping(value = "deleteImage/{id}", method = RequestMethod.DELETE)
    public String deleteImage(@PathVariable("id") Long id) {
        Book book = service.findOne(id);
        book.setBookImage(null);
        service.update(book);
        return "book/edit/" + book.getId();
    }

    private void validateImage(MultipartFile image) {
        if (!image.getContentType().equals("image/jpeg")) {
            throw new ImageUploadException("Only JPEG images accepted");
        }
    }

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
