package org.khasanof.springbootstarterfluent.core.executors.matcher.impls;

import org.khasanof.springbootstarterfluent.core.executors.matcher.GenericMatcher;
import org.khasanof.springbootstarterfluent.main.annotation.methods.chat.HandleMyChatMember;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.executors.matcher.impls
 * @since 1/2/2024 7:42 PM
 */
@Component
public class DefaultMyChatMemberMatcher extends GenericMatcher<HandleMyChatMember, ChatMemberUpdated> {

    @Override
    public boolean matcher(HandleMyChatMember annotation, ChatMemberUpdated value) {
        return true;
    }

    @Override
    public Class<HandleMyChatMember> getType() {
        return HandleMyChatMember.class;
    }
}
