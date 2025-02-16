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
import com.juaracoding.tugasakhir.core.IReportForm;
import com.juaracoding.tugasakhir.core.IService;
import com.juaracoding.tugasakhir.dto.response.RespMenuDTO;
import com.juaracoding.tugasakhir.dto.table.TableMenuDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValMenuDTO;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Menu;
import com.juaracoding.tugasakhir.repository.MenuRepository;
import com.juaracoding.tugasakhir.util.*;
import com.juaracoding.tugasakhir.util.GlobalFunction;
import com.juaracoding.tugasakhir.util.GlobalResponse;
import com.juaracoding.tugasakhir.util.LoggingFile;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class MenuServiceImpl implements IService<Menu>,IReportForm<Menu> {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MenuRepository menuRepo;

    @Autowired
    private TransformPagination transformPagination;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    private StringBuilder sbuild = new StringBuilder();

    @Override
    public ResponseEntity<Object> save(Menu menu, HttpServletRequest request) {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        try{
            if(menu==null){
                return GlobalResponse.dataTidakValid("FVAUT02001",request);
            }
            menu.setCreatedBy(token.get("username").toString());
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
        Map<String,Object> token = GlobalFunction.extractToken(request);
        try{
            if(menu==null){
                return GlobalResponse.dataTidakValid("FVAUT02011",request);
            }
            Optional<Menu> menuOptional = menuRepo.findById(id);
            if(!menuOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            Menu menuDB = menuOptional.get();
            menuDB.setUpdatedBy(token.get("username").toString());
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
        Map<String,Object> token = GlobalFunction.extractToken(request);
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
        Page<Menu> page = null;
        List<Menu> list = null;
        page = menuRepo.findAll(pageable);
        list = page.getContent();
//        List<RespMenuDTO> listDTO = convertToListRespMenuDTO(list);
        List<TableMenuDTO> listDTO = convertToTableMenuDTO(list);

        if(list.isEmpty()){
            return GlobalResponse.dataTidakDitemukan(request);
        }
        Map<String, Object> mapList = transformPagination.transformPagination(listDTO,page,"id","");
        return GlobalResponse.dataResponseList(mapList,request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        RespMenuDTO respMenuDTO;
        try{
            Optional<Menu> menuOptional = menuRepo.findById(id);
            if(!menuOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            Menu menuDB = menuOptional.get();
            respMenuDTO = modelMapper.map(menuDB, RespMenuDTO.class);
        }catch (Exception e){
            LoggingFile.logException("MenuService","findById --> Line 162",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDiakses("FEAUT02041",request);
        }
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.OK,
                respMenuDTO,null,request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<Menu> page = null;
        List<Menu> list = null;
        switch(columnName){

            case "name": page = menuRepo.findByNameContainsIgnoreCase(pageable,value);break;
            case "path": page = menuRepo.findByPathContainsIgnoreCase(pageable,value);break;
            //case "group": page = menuRepo.cariGroupMenu(pageable,value);break;
            default : page = menuRepo.findAll(pageable);break;
        }
        list = page.getContent();
        if(list.isEmpty()){
            return GlobalResponse.dataTidakDitemukan(request);
        }
//        List<RespMenuDTO> listDTO = convertToListRespMenuDTO(list);
        List<TableMenuDTO> listDTO = convertToTableMenuDTO(list);
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
            menuRepo.saveAll(convertListWorkBookToListEntity(lt,Long.parseLong(token.get("userId").toString())));
        }catch (Exception e){
            LoggingFile.logException("MenuService","upload excel --> Line 213",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.fileExcelGagalDiproses("FEAUT02061",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    public List<Menu> convertListWorkBookToListEntity(List<Map<String, String>> workBookData, Long userId) {
        List<Menu> list = new ArrayList<>();
        for (int i = 0; i < workBookData.size(); i++) {
            Map<String, String> map = workBookData.get(i);
            Menu menu = new Menu();
            menu.setName(map.get("nama-menu"));//setting header column
            menu.setPath(map.get("path"));
            menu.setCreatedBy(String.valueOf(userId));
            menu.setCreatedDate(new Date());
            list.add(menu);
        }
        return list;
    }

    @Override
    public void downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        List<Menu> menuList = null;
        switch (column){
            case "name":menuList= menuRepo.findByNameContainsIgnoreCase(value);break;
            case "path":menuList= menuRepo.findByPathContainsIgnoreCase(value);break;
            default:menuList= menuRepo.findAll();break;
        }
        /** menggunakan response karena sama untuk report */
        List<RespMenuDTO> respMenuDTOList = convertToListRespMenuDTO(menuList);
        if(respMenuDTOList.isEmpty()){
            GlobalResponse.manualResponse(response,GlobalResponse.dataTidakDitemukan(request));
            return;
        }

        sbuild.setLength(0);
        String headerKey = "Content-Disposition";
        sbuild.setLength(0);

        String headerValue = sbuild.append("attachment; filename=menu_").
                append(new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss.SSS").format(new Date())).append(".xlsx").toString();
        response.setHeader(headerKey, headerValue);
        response.setContentType("application/octet-stream");

        Map<String,Object> map = GlobalFunction.convertClassToObject(new RespMenuDTO());
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
        int listRespMenuDTOSize = respMenuDTOList.size();
        String [][] strBody = new String[listRespMenuDTOSize][intListTemp];
        for(int i=0;i<listRespMenuDTOSize;i++){
            map = GlobalFunction.convertClassToObject(respMenuDTOList.get(i));
            for(int j=0;j<intListTemp;j++){
                strBody[i][j] = String.valueOf(map.get(loopDataArr[j]));
            }
        }
        new ExcelWriter(strBody,headerArr,"sheet-1",response);
    }

    @Override
    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        List<Menu> menuList = null;
        switch (column){
            case "nama":menuList= menuRepo.findByNameContainsIgnoreCase(value);break;
            default:menuList= menuRepo.findAll();break;
        }
        /** menggunakan response karena sama untuk report */
        List<RespMenuDTO> respMenuDTOList = convertToListRespMenuDTO(menuList);
        int intRespMenuDTOList = respMenuDTOList.size();

        if(respMenuDTOList.isEmpty()){
            GlobalResponse.manualResponse(response,GlobalResponse.dataTidakDitemukan(request));
            return;
        }

        /** INI OBJECT MAP FINAL */
        Map<String,Object> map = new HashMap<>();
        String strHtml = null;
        Context context = new Context();
        Map<String,Object> mapColumnName = GlobalFunction.convertClassToObject(new RespMenuDTO());
        List<String> listTemp = new ArrayList<>();
        List<String> listHelper = new ArrayList<>();
        for (Map.Entry<String,Object> entry : mapColumnName.entrySet()) {
            listTemp.add(GlobalFunction.camelToStandar(entry.getKey()));
            listHelper.add(entry.getKey());
        }
        Map<String,Object> mapTemp = null;
        List<Map<String,Object>> listMap = new ArrayList<>();
        for(int i=0;i<menuList.size();i++){
            mapTemp = GlobalFunction.convertClassToObject(menuList.get(i));
            listMap.add(mapTemp);
        }

        map.put("title","REPORT DATA MENU");
        map.put("listKolom",listTemp);
        map.put("listHelper",listHelper);
        map.put("timestamp",new Date());
        map.put("totalData",intRespMenuDTOList);
        map.put("listContent",listMap);
        map.put("username",token.get("username"));
        context.setVariables(map);
        strHtml = springTemplateEngine.process("global-report",context);
        pdfGenerator.htmlToPdf(strHtml,"menu",response);
    }

    public List<RespMenuDTO> convertToListRespMenuDTO(List<Menu> menuList){
        return modelMapper.map(menuList,new TypeToken<List<RespMenuDTO>>(){}.getType());
    }


    public List<TableMenuDTO> convertToTableMenuDTO(List<Menu> menuList){
        List<TableMenuDTO> list = new ArrayList<>();
        TableMenuDTO tableMenuDTO ;
        for(Menu menu : menuList){
            tableMenuDTO = new TableMenuDTO();
            tableMenuDTO.setId(menu.getId());
            tableMenuDTO.setName(menu.getName());
            tableMenuDTO.setPath(menu.getPath());
            list.add(tableMenuDTO);
        }
        return list;
    }


    public Menu convertToMenu(ValMenuDTO menuDTO){
        return modelMapper.map(menuDTO,Menu.class);
    }
}
