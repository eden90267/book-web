package my.eden.book.web.controller;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import my.eden.book.web.exception.ImageUploadException;
import my.eden.book.web.logic.AWSS3Service;
import my.eden.book.web.logic.BookService;
import my.eden.book.web.rest.client.vo.Book;
import org.jets3t.service.S3ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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

    @Autowired
    private AWSS3Service awsS3Service;

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
                awsS3Service.saveImage(bookImageName, image);
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
                awsS3Service.saveImage(bookImageName, image);
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

}
