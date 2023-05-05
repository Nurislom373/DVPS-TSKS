package uz.devops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.domain.InvoiceRequest;
import uz.devops.repository.InvoiceRequestRepository;
import uz.devops.service.InvoiceRequestService;
import uz.devops.service.dto.InvoiceRequestDTO;
import uz.devops.service.mapper.InvoiceRequestMapper;

/**
 * Service Implementation for managing {@link InvoiceRequest}.
 */
@Service
@Transactional
public class InvoiceRequestServiceImpl implements InvoiceRequestService {

    private final Logger log = LoggerFactory.getLogger(InvoiceRequestServiceImpl.class);

    private final InvoiceRequestRepository invoiceRequestRepository;

    private final InvoiceRequestMapper invoiceRequestMapper;

    public InvoiceRequestServiceImpl(InvoiceRequestRepository invoiceRequestRepository, InvoiceRequestMapper invoiceRequestMapper) {
        this.invoiceRequestRepository = invoiceRequestRepository;
        this.invoiceRequestMapper = invoiceRequestMapper;
    }

    @Override
    public InvoiceRequestDTO save(InvoiceRequestDTO invoiceRequestDTO) {
        log.debug("Request to save InvoiceRequest : {}", invoiceRequestDTO);
        InvoiceRequest invoiceRequest = invoiceRequestMapper.toEntity(invoiceRequestDTO);
        invoiceRequest = invoiceRequestRepository.save(invoiceRequest);
        return invoiceRequestMapper.toDto(invoiceRequest);
    }

    @Override
    public InvoiceRequestDTO update(InvoiceRequestDTO invoiceRequestDTO) {
        log.debug("Request to update InvoiceRequest : {}", invoiceRequestDTO);
        InvoiceRequest invoiceRequest = invoiceRequestMapper.toEntity(invoiceRequestDTO);
        invoiceRequest = invoiceRequestRepository.save(invoiceRequest);
        return invoiceRequestMapper.toDto(invoiceRequest);
    }

    @Override
    public Optional<InvoiceRequestDTO> partialUpdate(InvoiceRequestDTO invoiceRequestDTO) {
        log.debug("Request to partially update InvoiceRequest : {}", invoiceRequestDTO);

        return invoiceRequestRepository
            .findById(invoiceRequestDTO.getId())
            .map(existingInvoiceRequest -> {
                invoiceRequestMapper.partialUpdate(existingInvoiceRequest, invoiceRequestDTO);

                return existingInvoiceRequest;
            })
            .map(invoiceRequestRepository::save)
            .map(invoiceRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceRequestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceRequests");
        return invoiceRequestRepository.findAll(pageable).map(invoiceRequestMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceRequestDTO> findOne(Long id) {
        log.debug("Request to get InvoiceRequest : {}", id);
        return invoiceRequestRepository.findById(id).map(invoiceRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InvoiceRequest : {}", id);
        invoiceRequestRepository.deleteById(id);
    }
}
