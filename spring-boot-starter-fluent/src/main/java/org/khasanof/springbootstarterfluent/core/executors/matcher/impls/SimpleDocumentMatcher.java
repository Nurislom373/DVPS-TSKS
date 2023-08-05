package org.khasanof.springbootstarterfluent.core.executors.matcher.impls;

import org.khasanof.springbootstarterfluent.core.enums.scopes.DocumentScope;
import org.khasanof.springbootstarterfluent.core.executors.matcher.GenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleDocument;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 28.06.2023 10:27
 */
@Component
public class SimpleDocumentMatcher extends GenericMatcher<HandleDocument, Document> {

    private final Map<DocumentScope, Function<Document, Object>> biFunctionMap = new HashMap<>();

    {
        setFunctionMap();
    }

    @Override
    public boolean matcher(HandleDocument annotation, Document value) {
        Object apply = biFunctionMap.get(annotation.scope()).apply(value);
        return matchFunctions.get(Map.entry(annotation.match(), getScopeType(apply, annotation.match())))
                .apply(annotation.value(), apply);
    }

    @Override
    public Class<HandleDocument> getType() {
        return HandleDocument.class;
    }

    private void setFunctionMap() {
        biFunctionMap.put(DocumentScope.FILE_NAME, Document::getFileName);
        biFunctionMap.put(DocumentScope.MIME_TYPE, Document::getMimeType);
        biFunctionMap.put(DocumentScope.FILE_SIZE, Document::getFileSize);
    }


}
