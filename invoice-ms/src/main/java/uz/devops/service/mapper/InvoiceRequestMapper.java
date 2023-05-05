package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.InvoiceRequest;
import uz.devops.service.dto.InvoiceRequestDTO;

/**
 * Mapper for the entity {@link InvoiceRequest} and its DTO {@link InvoiceRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceRequestMapper extends EntityMapper<InvoiceRequestDTO, InvoiceRequest> {}
