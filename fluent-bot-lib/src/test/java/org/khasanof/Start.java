package org.khasanof;

import org.khasanof.main.FluentStarter;
import org.khasanof.main.annotation.HandlerScanner;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 14:18
 * <br/>
 * Package: org.khasanof
 */
@HandlerScanner(value = "org.khasanof")
public class Start {

    public static void main(String[] args) {
        FluentStarter.start();
    }

}