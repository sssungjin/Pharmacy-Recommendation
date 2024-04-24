package dev.lecture.pharmacy.service;

import dev.lecture.pharmacy.entity.Pharmacy;
import dev.lecture.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRepositoryService {

    private final PharmacyRepository pharmacyRepository;

    // self invocation test
//    public void bar(List<Pharmacy> pharmacyList) {
//        log.info("bar CurrentTransactionName: "+ TransactionSynchronizationManager.getCurrentTransactionName());
//        foo(pharmacyList);
//    }
//
//    // self invocation test
//    @Transactional
//    public void foo(List<Pharmacy> pharmacyList) {
//        log.info("foo CurrentTransactionName: "+ TransactionSynchronizationManager.getCurrentTransactionName());
//        pharmacyList.forEach(pharmacy -> {
//            pharmacyRepository.save(pharmacy);
//            throw new RuntimeException("error");
//        });
//    }


    // read only test
    @Transactional(readOnly = true)
    public void startReadOnlyMethod(Long id) {
        pharmacyRepository.findById(id).ifPresent(pharmacy ->
                pharmacy.changePharmacyAddress("서울 특별시 광진구"));
    }


    @Transactional
    public List<Pharmacy> saveAll(List<Pharmacy> pharmacyList) {
        if(CollectionUtils.isEmpty(pharmacyList)) return Collections.emptyList();
        return pharmacyRepository.saveAll(pharmacyList);
    }

    @Transactional
    public void updateAddress(Long id, String address) {
        Pharmacy entity = pharmacyRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)) {
            log.error("[PharmacyRepositoryService updateAddress] not found id : {}", id);
            return;
        }
        entity.changePharmacyAddress(address);
    }

    // for test
    public void updateAddressWithoutTransaction(Long id, String address) {
        Pharmacy entity = pharmacyRepository.findById(id).orElse(null);

        if(Objects.isNull(entity)) {
            log.error("[PharmacyRepositoryService updateAddress] not found id : {}", id);
            return;
        }
        entity.changePharmacyAddress(address);
    }

    @Transactional(readOnly = true) // readonly true 하면 더티 체킹 진행 안하기 때문에 성능 향상
    public List<Pharmacy> findAll() {
        return pharmacyRepository.findAll();
    }
}
