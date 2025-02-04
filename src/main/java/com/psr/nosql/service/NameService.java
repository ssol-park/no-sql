package com.psr.nosql.service;

import com.psr.nosql.constant.MessageConstant;
import com.psr.nosql.dto.NameDto;
import com.psr.nosql.entity.Name;
import com.psr.nosql.repository.NameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NameService {
    private final NameRepository nameRepository;

    public void saveName(NameDto nameDto) {
        nameRepository.save(new Name(nameDto.getEngName(), nameDto.getKorName()));
    }

    public String getKorName(String engName) {
        return nameRepository.findById(engName)
                .map(Name::getKorName)
                .orElseThrow(() -> new RuntimeException(MessageConstant.NOT_FOUND));
    }
}
