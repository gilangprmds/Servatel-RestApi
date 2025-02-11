package com.juaracoding.tugasakhir.service.impl;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 20:46
@Last Modified 07/02/2025 20:46
Version 1.0
*/

import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.core.IService;
import com.juaracoding.tugasakhir.dto.response.RespRoleDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValRoleDTO;
import com.juaracoding.tugasakhir.model.Role;
import com.juaracoding.tugasakhir.repository.RoleRepository;
import com.juaracoding.tugasakhir.util.GlobalFunction;
import com.juaracoding.tugasakhir.util.GlobalResponse;
import com.juaracoding.tugasakhir.util.LoggingFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoleServiceImpl implements IService<Role> {

    @Autowired
    private ModelMapper modelMapper ;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public ResponseEntity<Object> save(Role role, HttpServletRequest request) {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        try{
            if(role==null){
                return GlobalResponse.dataTidakValid("FVAUT03001",request);
            }
            role.setCreatedBy(token.get("firstName").toString());
            role.setCreatedDate(new Date());
            role.setLtMenu(role.getLtMenu());
            roleRepo.save(role);
        }catch (Exception e){
            LoggingFile.logException("AksesService","save --> Line 42",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDisimpan("FEAUT03001",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> update(Long id, Role role, HttpServletRequest request) {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        try{
            if(role==null){
                return GlobalResponse.dataTidakValid("FVAUT03011",request);
            }
            Optional<Role> roleOptional = roleRepo.findById(id);
            if(!roleOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            Role roleDB = roleOptional.get();
            roleDB.setUpdatedBy(token.get("firstName").toString());
            roleDB.setUpdatedDate(new Date());
            roleDB.setRole(role.getRole());
            roleDB.setLtMenu(role.getLtMenu());

        }catch (Exception e){
            LoggingFile.logException("AksesService","update --> Line 75",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDiubah("FEAUT03011",request);
        }
        return GlobalResponse.dataBerhasilDiubah(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        try{
            Optional<Role> roleOptional = roleRepo.findById(id);
            if(!roleOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            roleRepo.deleteById(id);
        }catch (Exception e){
            LoggingFile.logException("AksesService","delete --> Line 111",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDiubah("FEAUT03021",request);
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


    public Role convertToRole(ValRoleDTO roleDTO){
        return modelMapper.map(roleDTO,Role.class);
    }
    public List<RespRoleDTO> convertToListRespRoleDTO(List<Role> roleList){
        return modelMapper.map(roleList,new TypeToken<List<RespRoleDTO>>(){}.getType());
    }

}
