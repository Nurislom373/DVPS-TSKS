package org.khasanof.core.collector;

import org.khasanof.core.collector.impls.CommonMethodAdapter;
import org.khasanof.core.collector.questMethod.QuestMethod;
import org.khasanof.core.collector.questMethod.impls.AsyncQuestMethod;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector
 * @since 25.06.2023 21:04
 */
public abstract class AbstractCollector {

    protected final CommonMethodAdapter methodAdapter = new CommonMethodAdapter();

    protected final QuestMethod questMethod = new AsyncQuestMethod();

}
