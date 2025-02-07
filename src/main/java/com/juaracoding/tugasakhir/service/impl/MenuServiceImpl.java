package com.juaracoding.tugasakhir.service.impl;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 22:16
@Last Modified 07/02/2025 22:16
Version 1.0
*/

import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.core.IService;
import com.juaracoding.tugasakhir.dto.validasi.ValMenuDTO;
import com.juaracoding.tugasakhir.model.Menu;
import com.juaracoding.tugasakhir.repository.MenuRepository;
import com.juaracoding.tugasakhir.util.GlobalResponse;
import com.juaracoding.tugasakhir.util.LoggingFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class MenuServiceImpl implements IService<Menu> {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MenuRepository menuRepo;

    @Override
    public ResponseEntity<Object> save(Menu menu, HttpServletRequest request) {
        try{
            if(menu==null){
                return GlobalResponse.dataTidakValid("FVAUT02001",request);
            }
            menu.setCreatedBy("Paul");
            menu.setCreatedDate(new Date());
            menuRepo.save(menu);
        }catch (Exception e){
            LoggingFile.logException("MenuService","save --> Line 42",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDisimpan("FEAUT02001",request);
        }

        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> update(Long id, Menu menu, HttpServletRequest request) {
        try{
            if(menu==null){
                return GlobalResponse.dataTidakValid("FVAUT02011",request);
            }
            Optional<Menu> menuOptional = menuRepo.findById(id);
            if(!menuOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            Menu menuDB = menuOptional.get();
            menuDB.setUpdatedBy("Reksa");
            menuDB.setUpdatedDate(new Date());
            menuDB.setName(menu.getName());
            menuDB.setPath(menu.getPath());


        }catch (Exception e){
            LoggingFile.logException("MenuService","update --> Line 75",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDiubah("FEAUT02011",request);
        }
        return GlobalResponse.dataBerhasilDiubah(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try{
            Optional<Menu> menuOptional = menuRepo.findById(id);
            if(!menuOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            menuRepo.deleteById(id);
        }catch (Exception e){
            LoggingFile.logException("MenuService","delete --> Line 111",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDiubah("FEAUT02021",request);
        }
        return GlobalResponse.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        return null;
    }


    public Menu convertToMenu(ValMenuDTO menuDTO){
        return modelMapper.map(menuDTO,Menu.class);
    }
}
