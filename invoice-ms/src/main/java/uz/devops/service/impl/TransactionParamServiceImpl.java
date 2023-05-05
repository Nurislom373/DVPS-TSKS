package uz.devops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.domain.TransactionParam;
import uz.devops.repository.TransactionParamRepository;
import uz.devops.service.TransactionParamService;
import uz.devops.service.dto.TransactionParamDTO;
import uz.devops.service.mapper.TransactionParamMapper;

/**
 * Service Implementation for managing {@link TransactionParam}.
 */
@Service
@Transactional
public class TransactionParamServiceImpl implements TransactionParamService {

    private final Logger log = LoggerFactory.getLogger(TransactionParamServiceImpl.class);

    private final TransactionParamRepository transactionParamRepository;

    private final TransactionParamMapper TransactionParamMapper;

    public TransactionParamServiceImpl(TransactionParamRepository transactionParamRepository, TransactionParamMapper TransactionParamMapper) {
        this.transactionParamRepository = transactionParamRepository;
        this.TransactionParamMapper = TransactionParamMapper;
    }

    @Override
    public TransactionParamDTO save(TransactionParamDTO transactionParamDTO) {
        log.debug("Request to save TransactionParam : {}", transactionParamDTO);
        TransactionParam transactionParam = TransactionParamMapper.toEntity(transactionParamDTO);
        transactionParam = transactionParamRepository.save(transactionParam);
        return TransactionParamMapper.toDto(transactionParam);
    }

    @Override
    public TransactionParamDTO update(TransactionParamDTO transactionParamDTO) {
        log.debug("Request to update TransactionParam : {}", transactionParamDTO);
        TransactionParam transactionParam = TransactionParamMapper.toEntity(transactionParamDTO);
        transactionParam = transactionParamRepository.save(transactionParam);
        return TransactionParamMapper.toDto(transactionParam);
    }

    @Override
    public Optional<TransactionParamDTO> partialUpdate(TransactionParamDTO transactionParamDTO) {
        log.debug("Request to partially update TransactionParam : {}", transactionParamDTO);

        return transactionParamRepository
            .findById(transactionParamDTO.getId())
            .map(existingTransactionParam -> {
                TransactionParamMapper.partialUpdate(existingTransactionParam, transactionParamDTO);

                return existingTransactionParam;
            })
            .map(transactionParamRepository::save)
            .map(TransactionParamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionParamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionParams");
        return transactionParamRepository.findAll(pageable).map(TransactionParamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionParamDTO> findOne(Long id) {
        log.debug("Request to get TransactionParam : {}", id);
        return transactionParamRepository.findById(id).map(TransactionParamMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionParam : {}", id);
        transactionParamRepository.deleteById(id);
    }
}
