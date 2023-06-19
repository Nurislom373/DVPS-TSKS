package org.khasanof.core.publisher;

import org.khasanof.core.publisher.methods.delete.DeleteOperators;
import org.khasanof.core.publisher.methods.send.SenderOperators;
import org.khasanof.core.publisher.methods.update.UpdateOperators;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 22:02
 * <br/>
 * Package: org.khasanof.core.executors
 */
public abstract class Publisher {

    public static SenderOperators send() {
        return new SenderOperators();
    }

    public static DeleteOperators delete() {
        return new DeleteOperators();
    }

    public static UpdateOperators update() {
        return new UpdateOperators();
    }

}
