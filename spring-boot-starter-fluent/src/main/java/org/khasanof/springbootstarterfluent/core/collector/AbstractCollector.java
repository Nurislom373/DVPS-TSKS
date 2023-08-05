package org.khasanof.springbootstarterfluent.core.collector;


import org.khasanof.springbootstarterfluent.core.collector.questMethod.QuestMethod;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector
 * @since 25.06.2023 21:04
 */
public abstract class AbstractCollector {

    protected final CommonMethodAdapter methodAdapter;
    protected final QuestMethod questMethod;

    public AbstractCollector(CommonMethodAdapter methodAdapter, QuestMethod questMethod) {
        this.methodAdapter = methodAdapter;
        this.questMethod = questMethod;
    }
}
