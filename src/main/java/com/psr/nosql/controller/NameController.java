package com.psr.nosql.controller;

import com.psr.nosql.dto.NameDto;
import com.psr.nosql.dto.ResponseDto;
import com.psr.nosql.service.NameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/names")
public class NameController {

    private final NameService nameService;

    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody NameDto nameDto) {
        nameService.saveName(nameDto);

        return ResponseEntity.ok(new ResponseDto());
    }

    @GetMapping
    public ResponseEntity<ResponseDto> findKorName(@RequestParam String engName) {
        String korName = nameService.getKorName(engName);
        return ResponseEntity.ok(new ResponseDto(korName));
    }
}
