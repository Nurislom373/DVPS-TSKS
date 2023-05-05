package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.TransactionParam;
import uz.devops.service.dto.TransactionParamDTO;

/**
 * Mapper for the entity {@link TransactionParam} and its DTO {@link TransactionParamDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionParamMapper extends EntityMapper<TransactionParamDTO, TransactionParam> {}
