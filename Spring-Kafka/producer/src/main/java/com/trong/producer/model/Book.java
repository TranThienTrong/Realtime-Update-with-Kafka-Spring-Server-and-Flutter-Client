package com.trong.producer.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class Book {
    private Integer bookId;
    private String bookName;
    private String bookAuthor;
}
