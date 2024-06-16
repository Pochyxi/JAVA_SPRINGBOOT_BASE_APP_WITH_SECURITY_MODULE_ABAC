package com.app.security.serviceImpl;

import com.app.security.exception.ErrorCodeList;
import com.app.security.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public class BaseServiceImpl<E, I> {

    private JpaRepository<E, I> baseRepository;

    public E findById(I id) {
        return  baseRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException( ErrorCodeList.NF404));
    }
}
