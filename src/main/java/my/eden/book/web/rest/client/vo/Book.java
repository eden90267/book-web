package my.eden.book.web.rest.client.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by eden90267 on 2016/5/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Getter
@Setter
public class Book {

    private long id;
    private String bookName;
    private BigDecimal bookPrice;
    private String bookImage;
    private Date createTime;

    private Book(Builder builder) {
        setId(builder.id);
        setBookName(builder.bookName);
        setBookPrice(builder.bookPrice);
        setBookImage(builder.bookImage);
        setCreateTime(builder.createTime);
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private long id;
        private String bookName;
        private BigDecimal bookPrice;
        private String bookImage;
        private Date createTime;

        private Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder bookName(String val) {
            bookName = val;
            return this;
        }

        public Builder bookPrice(BigDecimal val) {
            bookPrice = val;
            return this;
        }

        public Builder bookImage(String val) {
            bookImage = val;
            return this;
        }

        public Builder createTime(Date val) {
            createTime = val;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
