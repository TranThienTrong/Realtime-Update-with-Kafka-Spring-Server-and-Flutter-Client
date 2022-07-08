package com.trong.producer.model;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
public class LibraryEvent {

    private Integer libraryEventId;
    private Book book;
    private LibraryEventType libraryEventType;


}
