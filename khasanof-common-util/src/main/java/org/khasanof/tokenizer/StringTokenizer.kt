package org.khasanof.tokenizer

import org.khasanof.tokenizer.StringTokenizerUtils.getTokensWithGeneric

fun getTokensWithList(string: String, delim: String): List<String> {
    return getTokensWithGeneric(string, delim, false, ::ArrayList)
}

fun getTokensWithList(string: String, delim: String, returnDelim: Boolean): List<String> {
    return getTokensWithGeneric(string, delim, returnDelim, ::ArrayList)
}
