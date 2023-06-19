package org.khasanof.main.inferaces.executor;

import lombok.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 19.06.2023
 * <br/>
 * Time: 23:04
 * <br/>
 * Package: org.khasanof.main.inferaces.executor
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Executable<T> {

    private T data;
    private Type type;


    enum Type {
        SEND_MESSAGE, SEND_VIDEO, SEND_PHOTO
    }
}
