package com.juaracoding.tugasakhir.service.impl;


import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.core.IService;
import com.juaracoding.tugasakhir.dto.response.RespUserDTO;
import com.juaracoding.tugasakhir.dto.table.TableUserByRoleDTO;
import com.juaracoding.tugasakhir.dto.table.TableUserDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValSetChangePasswordDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValUserDTO;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Role;
import com.juaracoding.tugasakhir.model.User;
import com.juaracoding.tugasakhir.repository.UserRepository;
import com.juaracoding.tugasakhir.security.BcryptImpl;
import com.juaracoding.tugasakhir.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 06/02/2025 14:26
@Last Modified 06/02/2025 14:26
Version 1.0
*/

@Service
public class UserServiceImpl implements IService<User> {


    /**
     * Platform Code : AUT
     * Modul Code : 02
     * FV = Failed Validation
     * FE = Failed Error
     */

    @Autowired
    private ModelMapper modelMapper ;

    @Autowired
    UserRepository userRepo;

    @Autowired
    private TransformPagination transformPagination;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    private Random random = new Random();

    private StringBuilder sbuild = new StringBuilder();


    @Override
    public ResponseEntity<Object> save(User user, HttpServletRequest request) {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        try{
            if(user==null){
                return GlobalResponse.dataTidakValid("FVAUT02001",request);
            }

//            if(user.getRole()==null){
//                Role role = new Role();\cv
//                role.setId(2L);
//                user.setRole(role);
//            }
            Role role = new Role();
            role.setId(3L);
            user.setRole(role);
            user.setIsRegistered(true);
            user.setIsActive(true);
            user.setPassword(BcryptImpl.hash(user.getUsername()+user.getPassword()));
            user.setCreatedBy(token.get("username").toString());
            user.setCreatedDate(new Date());
            userRepo.save(user);
        }catch (Exception e){
            LoggingFile.logException("UserService","save --> Line 42",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDisimpan("FEAUT02001",request);
        }
        return GlobalResponse.dataBerhasilDisimpan(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> update(Long id, User user, HttpServletRequest request) {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        try{
            if(user==null){
                return GlobalResponse.dataTidakValid("FVAUT02011",request);
            }
            Optional<User> userOptional = userRepo.findById(id);
            if(!userOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            User userDB = userOptional.get();
            userDB.setUsername(user.getUsername());
            userDB.setUpdatedBy(token.get("username")+token.get("lastName").toString());
            userDB.setUpdatedDate(new Date());
            userDB.setFirstName(user.getFirstName());
            userDB.setLastName(user.getLastName());
            userDB.setAddress(user.getAddress());
            userDB.setNoHp(user.getNoHp());
            userDB.setEmail(user.getEmail());
//            userDB.setPassword(BcryptImpl.hash(userDB.getUsername()+ userDB.getTanggalLahir().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
//            userDB.setRole(user.getRole());//ini relasi nya
            userRepo.save(userDB);
        }catch (Exception e){
            LoggingFile.logException("UserService","update --> Line 75",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDiubah("FEAUT02011",request);
        }
        return GlobalResponse.dataBerhasilDiubah(request);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try{
            Optional<User> userOptional = userRepo.findById(id);
            if(!userOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            User userDB = userOptional.get();
            userDB.setIsActive(false);
        }catch (Exception e){
            LoggingFile.logException("UserService","delete --> Line 111",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDiubah("FEAUT02021",request);
        }
        return GlobalResponse.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<User> page = null;
        List<User> list = null;
        page = userRepo.findAllByIsActiveTrue(pageable);
        list = page.getContent();
//        List<User> activeUser = list.stream()
//                .filter(user -> user.getIsActive() == true)
//                .collect(Collectors.toList());
//        List<RespUserDTO> listDTO = convertToListRespUserDTO(list);
        List<TableUserDTO> listDTO = convertToTableUserDTO(list);

        if(list.isEmpty()){
            return GlobalResponse.dataTidakDitemukan(request);
        }
        Map<String, Object> mapList = transformPagination.transformPagination(listDTO,page,"id","");
        return GlobalResponse.dataResponseList(mapList,request);
    }

    public ResponseEntity<Object> findAllManagerOrCustomer(Pageable pageable, Long id, HttpServletRequest request) {
        Page<User> page = null;
        List<User> list = null;
        List<TableUserDTO> listDTO;
        try {
            page = userRepo.findByRole_IdAndIsActiveTrue(pageable, id);
            list = page.getContent();
//            List<User> activeUser = list.stream()
//                    .filter(user -> user.getIsActive() == true)
//                    .collect(Collectors.toList());
//        List<RespUserDTO> listDTO = convertToListRespUserDTO(list);
            listDTO = convertToTableUserDTO(list);
            if(list.isEmpty()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> mapList = transformPagination.transformPagination(listDTO,page,"id","");
        return GlobalResponse.dataResponseList(mapList,request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {

        RespUserDTO respUserDTO;
        try{
            Optional<User> userOptional = userRepo.findById(id);
            if(!userOptional.isPresent()){
                return GlobalResponse.dataTidakDitemukan(request);
            }
            User userDB = userOptional.get();
            respUserDTO = modelMapper.map(userDB, RespUserDTO.class);
        }catch (Exception e){
            LoggingFile.logException("UserService","findById --> Line 162",e, OtherConfig.getEnableLogFile());
            return GlobalResponse.dataGagalDiakses("FEAUT04041",request);
        }
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.OK,
                respUserDTO,null,request);
    }

    public ResponseEntity<Object> findByNameAndId(Pageable pageable, String name, Long id, HttpServletRequest request) {
        Page<User> page = null;
        List<User> list = null;
        page = userRepo.searchByNameAndId(name, id, pageable);
        list = page.getContent();
//        List<User> activeUser = list.stream()
//                .filter(user -> user.getIsActive() == true)
//                .collect(Collectors.toList());
//        List<RespUserDTO> listDTO = convertToListRespUserDTO(list);
        List<TableUserDTO> listDTO = convertToTableUserDTO(list);

        if(list.isEmpty()){
            return GlobalResponse.dataTidakDitemukan(request);
        }
        Map<String, Object> mapList = transformPagination.transformPagination(listDTO,page,"id","");
        return GlobalResponse.dataResponseList(mapList,request);
    }

    public ResponseEntity<Object> findByName(Pageable pageable, String name, HttpServletRequest request) {
        Page<User> page = null;
        List<User> list = null;
        page = userRepo.searchByName(name, pageable);
        list = page.getContent();
//        List<User> activeUser = list.stream()
//                .filter(user -> user.getIsActive() == true)
//                .collect(Collectors.toList());
//        List<RespUserDTO> listDTO = convertToListRespUserDTO(list);
        List<TableUserDTO> listDTO = convertToTableUserDTO(list);

        if(list.isEmpty()){
            return GlobalResponse.dataTidakDitemukan(request);
        }
        Map<String, Object> mapList = transformPagination.transformPagination(listDTO,page,"id","");
        return GlobalResponse.dataResponseList(mapList,request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<User> page = null;
        List<User> list = null;
        switch(columnName){
            case "firstName": page = userRepo.findByFirstNameContainsIgnoreCase(pageable,value);break;
            case "lastName": page = userRepo.findByLastNameContainsIgnoreCase(pageable,value);break;
            case "noHp": page = userRepo.findByNoHpContainsIgnoreCase(pageable,value);break;
            case "address": page = userRepo.findByAddressContainsIgnoreCase(pageable,value);break;
            case "email": page = userRepo.findByEmailContainsIgnoreCase(pageable,value);break;
            case "username": page = userRepo.findByUsernameContainsIgnoreCase(pageable,value);break;
            //case "password": page = userRepo.findByPasswordContainsIgnoreCase(pageable,value);break;
            case "umur": page = userRepo.cariUmur(pageable,value);break;
            default : page = userRepo.findAll(pageable);break;
        }
        list = page.getContent();
        if(list.isEmpty()){
            return GlobalResponse.dataTidakDitemukan(request);
        }
//        List<RespUserDTO> listDTO = convertToListRespUserDTO(list);
        List<TableUserDTO> listDTO = convertToTableUserDTO(list);
        Map<String, Object> mapList = transformPagination.transformPagination(listDTO,page,columnName,value);
        return GlobalResponse.dataResponseList(mapList,request);
    }



    @Transactional
    public ResponseEntity<Object>newOtp(HttpServletRequest request) throws UsernameNotFoundException {
        Integer otp = null;
        Map<String,Object> token = GlobalFunction.extractToken(request);
        Optional<User> optionalUser = userRepo.findByUsername(token.get("username").toString());

        User userDB = optionalUser.get();
        otp = random.nextInt(111111,999999);
        userDB.setOtp(BcryptImpl.hash(String.valueOf(otp)));
        SendMailOTP.verifyNewOtp("OTP Ganti Password",userDB.getFirstName(),userDB.getEmail(),String.valueOf(otp));
        return new ResponseHandler().handleResponse("OTP Berhasil Dikirim",
                HttpStatus.OK,
                null,null,request);
    }

    @Transactional
    public ResponseEntity<Object> setChangePassword(ValSetChangePasswordDTO user, HttpServletRequest request) throws UsernameNotFoundException {
        Map<String,Object> token = GlobalFunction.extractToken(request);
        if (user == null) {
            return new ResponseHandler().handleResponse("DATA TIDAK VALID",
                    HttpStatus.BAD_REQUEST,
                    null, "X01009", request);
        }
        Optional<User> optionalUser = userRepo.findByUsername(token.get("username").toString());
        User userDB = optionalUser.get();
        if (!BcryptImpl.verifyHash(user.getOtp(),userDB.getOtp())) {
            return new ResponseHandler().handleResponse("OTP TIDAK VALID",
                    HttpStatus.BAD_REQUEST,
                    null, "X01008", request);
        }
        if (!optionalUser.isPresent()) {
            return new ResponseHandler().handleResponse("USERNAME TIDAK VALID",
                    HttpStatus.BAD_REQUEST,
                    null, "X01008", request);
        }
        if (!(user.getPasswordBaru().toString()).equals((user.getKonfirmasiPasswordBaru()).toString())) {
            return new ResponseHandler().handleResponse("PASSWORD TIDAK VALID",
                    HttpStatus.BAD_REQUEST,
                    null, "X01010", request);
        }
        userDB.setPassword(BcryptImpl.hash(userDB.getUsername()+user.getPasswordBaru()));
        return new ResponseHandler().handleResponse("Password Berhasil Diubah",
                HttpStatus.OK,
                null,null,request);
    }


//    @Transactional
//    public ResponseEntity<Object> uploadImage(String username,MultipartFile file,HttpServletRequest request){
//        Map map ;
//        Map<String,Object> mapResponse ;
//        Optional<User> userOptional = userRepo.findByUsername(username);
//        if(!userOptional.isPresent()){
//            return GlobalResponse.dataTidakDitemukan(request);
//        }
//        rootPath = Paths.get(BASE_URL_IMAGE+"/"+new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date()));
//        String strPathz = rootPath.toAbsolutePath().toString();
//        String strPathzImage = strPathz+"\\"+file.getOriginalFilename();
//        save(file);
//        try {
//            map = cloudinary.uploader().upload(strPathzImage, ObjectUtils.asMap("public_id",file.getOriginalFilename()));
//            User userDB = userOptional.get();
//            userDB.setUpdatedBy(String.valueOf(userDB.getId()));
//            userDB.setUpdatedDate(new Date());
//            userDB.setPathImage(strPathzImage);
//            userDB.setLinkImage(map.get("secure_url").toString());
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }
//        Map<String,Object> m = new HashMap<>();
//        m.put("url-img",map.get("secure_url").toString());
//        return ResponseEntity.status(HttpStatus.OK).body(m);
////        return GlobalResponse.dataResponseObject(m,request);
//    }

    public List<RespUserDTO> convertToListRespUserDTO(List<User> userList){
        return modelMapper.map(userList,new TypeToken<List<RespUserDTO>>(){}.getType());
    }

    public List<TableUserDTO> convertToTableUserDTO(List<User> userList){
        List<TableUserDTO> tableUserDTOList = new ArrayList<>();
        TableUserDTO tableUserDTO = null;
        for(User user : userList){
            tableUserDTO = new TableUserDTO();
            tableUserDTO.setId(user.getId());
            tableUserDTO.setRoleType(user.getRole().getRoleType());
            tableUserDTO.setNoHp(user.getNoHp());
            tableUserDTO.setAddress(user.getAddress());
//            tableUserDTO.setPassword(user.getPassword());
            tableUserDTO.setEmail(user.getEmail());
            tableUserDTO.setUsername(user.getUsername());
            tableUserDTO.setFirstName(user.getFirstName());
            tableUserDTO.setLastName(user.getLastName());
            tableUserDTO.setUmur(user.getUmur());
            tableUserDTO.setTanggalLahir(user.getTanggalLahir().format(DateTimeFormatter.ISO_DATE.ofPattern("dd LLLL yyyy")));
            tableUserDTOList.add(tableUserDTO);
        }
        return tableUserDTOList;
    }

//    public List<TableUserDTO> convertToTableUserByRoleDTO(List<User> userList){
//        List<TableUserByRoleDTO> tableUserByRoleDTOList = new ArrayList<>();
//        TableUserByRoleDTO tableUserDTO = null;
//        for(User user : userList){
//            tableUserDTO = new TableUserByRoleDTO();
//            tableUserDTO.setId(user.getId());
//            tableUserDTO.setUsername(user.getUsername());
//            tableUserDTO.setFirstName(user.getFirstName());
//            tableUserDTO.setLastName(user.getLastName());
//            tableUserByRoleDTOList.add(tableUserDTO);
//        }
//        return tableUserByRoleDTOList;
//    }

    public User convertToUser(ValUserDTO userDTO){
        return modelMapper.map(userDTO,User.class);
    }
}


