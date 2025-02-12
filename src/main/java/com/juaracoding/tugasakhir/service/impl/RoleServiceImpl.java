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
import com.juaracoding.tugasakhir.core.IReportForm;
import com.juaracoding.tugasakhir.core.IService;
import com.juaracoding.tugasakhir.dto.response.RespRoleDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValRoleDTO;
import com.juaracoding.tugasakhir.enums.RoleType;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Role;
import com.juaracoding.tugasakhir.repository.RoleRepository;
import com.juaracoding.tugasakhir.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RoleServiceImpl implements IService<Role>, IReportForm<Role> {

    @Autowired
    private ModelMapper modelMapper ;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private TransformPagination transformPagination;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    private StringBuilder sbuild = new StringBuilder();

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
            roleDB.setRoleType(role.getRoleType());
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
        Page<Role> page = null;
        List<Role> list = null;
        page = roleRepo.findAll(pageable);
        list = page.getContent();
        List<RespRoleDTO> listDTO = convertToListRespRoleDTO(list);

        if(list.isEmpty()){
            return GlobalResponse.dataTidakDitemukan(request);
        }

        Map<String, Object> mapList = transformPagination.transformPagination(listDTO,page,"id","");
        return GlobalResponse.dataResponseList(mapList,request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        RespRoleDTO respRoleDTO;
        try{
            Optional<Role> roleOptional = roleRepo.findById(id);
            if(!roleOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            Role roleDB = roleOptional.get();
            respRoleDTO = modelMapper.map(roleDB, RespRoleDTO.class);
        }catch (Exception e){
            LoggingFile.logException("AksesService","findById --> Line 162",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDiakses("FEAUT03041",request);
        }
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.OK,
                respRoleDTO,null,request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<Role> page = null;
        List<Role> list = null;
        switch(columnName){

            case "roleType": page = roleRepo.findByRoleType(pageable,value);break;
            default : page = roleRepo.findAll(pageable);break;
        }
        list = page.getContent();
        if(list.isEmpty()){
            return GlobalResponse.dataTidakDitemukan(request);
        }
        List<RespRoleDTO> listDTO = convertToListRespRoleDTO(list);
        Map<String, Object> mapList = transformPagination.transformPagination(listDTO,page,columnName,value);
        return GlobalResponse.dataResponseList(mapList,request);
    }

    @Override
    public ResponseEntity<Object> uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request) {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        String message = "";
        if(!ExcelReader.hasWorkBookFormat(multipartFile)){
            return GlobalResponse.formatHarusExcel(request);
        }

        try{
            List lt = new ExcelReader(multipartFile.getInputStream(),"sheet1").getDataMap();
            if(lt.isEmpty()){
                if(ExcelReader.hasWorkBookFormat(multipartFile)){
                    return GlobalResponse.dataFileKosong(request);
                }
            }
            roleRepo.saveAll(convertListWorkBookToListEntity(lt,Long.parseLong(token.get("userId").toString())));
        }catch (Exception e){
            LoggingFile.logException("AksesService","upload excel --> Line 213",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.fileExcelGagalDiproses("FEAUT03061",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public List<Role> convertListWorkBookToListEntity(List<Map<String, String>> workBookData, Long userId) {
        List<Role> list = new ArrayList<>();
        for (int i = 0; i < workBookData.size(); i++) {
            Map<String, String> map = workBookData.get(i);
            Role role = new Role();
            role.setRoleType(RoleType.valueOf(map.get("name-role")));
            role.setCreatedBy(String.valueOf(userId));
            role.setCreatedDate(new Date());
            list.add(role);
        }
        return list;
    }

    @Override
    public void downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        List<Role> roleList = null;
        switch (column){
            case "roleType":roleList= roleRepo.findByRoleTypeContainsIgnoreCase(value);break;
//            case "group":menuList= menuRepo.cariGroupRole(value);break;
            default:roleList= roleRepo.findAll();break;
        }
        /** menggunakan response karena sama untuk report */
        List<RespRoleDTO> respGroupRoleDTOList = convertToListRespRoleDTO(roleList);
        if(respGroupRoleDTOList.isEmpty()){
            GlobalResponse.manualResponse(response,GlobalResponse.dataTidakDitemukan(request));
            return;
        }

        sbuild.setLength(0);
        String headerKey = "Content-Disposition";
        sbuild.setLength(0);

        String headerValue = sbuild.append("attachment; filename=group-menu_").
                append(new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss.SSS").format(new Date())).append(".xlsx").toString();
        response.setHeader(headerKey, headerValue);
        response.setContentType("application/octet-stream");

        Map<String,Object> map = GlobalFunction.convertClassToObject(new RespRoleDTO());
        List<String> listTemp = new ArrayList<>();
        for(Map.Entry<String,Object> entry : map.entrySet()){
            listTemp.add(entry.getKey());
        }

        int intListTemp = listTemp.size();
        String [] headerArr = new String[intListTemp];// kolom judul di excel
        String [] loopDataArr = new String[intListTemp];//kolom judul untuk java reflection

        /** Untuk mempersiapkan data judul kolom nya */
        for(int i=0;i<intListTemp;i++){
            headerArr[i] = GlobalFunction.camelToStandar(String.valueOf(listTemp.get(i))).toUpperCase();
            loopDataArr[i] = listTemp.get(i);
        }
        /** Untuk mempersiapkan data body baris nya */
        int listRespGroupRoleDTOSize = respGroupRoleDTOList.size();
        String [][] strBody = new String[listRespGroupRoleDTOSize][intListTemp];
        for(int i=0;i<listRespGroupRoleDTOSize;i++){
            map = GlobalFunction.convertClassToObject(respGroupRoleDTOList.get(i));
            for(int j=0;j<intListTemp;j++){
                strBody[i][j] = String.valueOf(map.get(loopDataArr[j]));
            }
        }
        new ExcelWriter(strBody,headerArr,"sheet-1",response);
    }

    @Override
    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        List<Role> menuList = null;
        switch (column){
            case "nama":menuList= roleRepo.findByRoleTypeContainsIgnoreCase(value);break;
//            case "group":menuList= menuRepo.cariGroupRole(value);break;
            default:menuList= roleRepo.findAll();break;
        }
        /** menggunakan response karena sama untuk report */
        List<RespRoleDTO> respGroupRoleDTOList = convertToListRespRoleDTO(menuList);
        int intRespGroupRoleDTOList = respGroupRoleDTOList.size();

        if(respGroupRoleDTOList.isEmpty()){
            GlobalResponse.manualResponse(response,GlobalResponse.dataTidakDitemukan(request));
            return;
        }


        /** INI OBJECT MAP FINAL */
        Map<String,Object> map = new HashMap<>();
        String strHtml = null;
        Context context = new Context();
        Map<String,Object> mapColumnName = GlobalFunction.convertClassToObject(new RespRoleDTO());
        List<String> listTemp = new ArrayList<>();
        List<String> listHelper = new ArrayList<>();
        for (Map.Entry<String,Object> entry : mapColumnName.entrySet()) {
            listTemp.add(GlobalFunction.camelToStandar(entry.getKey()));
            listHelper.add(entry.getKey());
        }
        Map<String,Object> mapTemp = null;
        List<Map<String,Object>> listMap = new ArrayList<>();
        for(int i=0;i<listTemp.size();i++){
            mapTemp = GlobalFunction.convertClassToObject(menuList.get(i));
            listMap.add(mapTemp);
        }

        map.put("title","REPORT DATA AKSES");
        map.put("listKolom",listTemp);
        map.put("listHelper",listHelper);
        map.put("timestamp",new Date());
        map.put("totalData",intRespGroupRoleDTOList);
        map.put("listContent",listMap);
        map.put("username",token.get("namaLengkap"));
        context.setVariables(map);
        strHtml = springTemplateEngine.process("global-report",context);
        pdfGenerator.htmlToPdf(strHtml,"role",response);
    }

    public Role convertToRole(ValRoleDTO roleDTO){
        return modelMapper.map(roleDTO,Role.class);
    }
    public List<RespRoleDTO> convertToListRespRoleDTO(List<Role> roleList){
        return modelMapper.map(roleList,new TypeToken<List<RespRoleDTO>>(){}.getType());
    }

}
