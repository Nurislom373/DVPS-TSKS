package org.khasanof.springbootstarterfluent.core.executors.matcher.impls;

import org.khasanof.springbootstarterfluent.core.executors.matcher.GenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.HandleDocuments;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.util.Arrays;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors.matcher.impls
 * @since 06.07.2023 22:35
 */
@Component
public class SimpleDocumentsMatcher extends GenericMatcher<HandleDocuments, Document> {

    private final SimpleDocumentMatcher matcher = new SimpleDocumentMatcher();

    @Override
    public boolean matcher(HandleDocuments annotation, Document value) {
        return Arrays.stream(annotation.values())
                .anyMatch(handleDocument -> matcher.matcher(handleDocument, value));
    }

    @Override
    public Class<HandleDocuments> getType() {
        return HandleDocuments.class;
    }
}
