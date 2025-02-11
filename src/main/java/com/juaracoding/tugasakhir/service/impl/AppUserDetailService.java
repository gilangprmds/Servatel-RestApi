package com.juaracoding.tugasakhir.service.impl;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 09/02/2025 13:17
@Last Modified 09/02/2025 13:17
Version 1.0
*/
import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.dto.response.RespMenuDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValLoginDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValRegisDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValVerifyRegisDTO;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Role;
import com.juaracoding.tugasakhir.model.User;
import com.juaracoding.tugasakhir.repository.UserRepository;
import com.juaracoding.tugasakhir.security.BcryptImpl;
import com.juaracoding.tugasakhir.security.Crypto;
import com.juaracoding.tugasakhir.security.JwtUtility;
import com.juaracoding.tugasakhir.util.SendMailOTP;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.patterns.IToken;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

//server ke database sourece lewat sini
@Service
@Transactional
public class AppUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;
    //    private ModelMapper modelMapper = new ModelMapper();
    private Random random = new Random();

    @Autowired
    private JwtUtility jwtUtility;

    public ResponseEntity<Object> login(User user, HttpServletRequest request) throws UsernameNotFoundException {
        Optional<User> optUser = userRepo.findByUsername(user.getUsername());
        if (!optUser.isPresent()) {
            return new ResponseHandler().handleResponse("USER TIDAK TERDAFTAR",
                    HttpStatus.BAD_REQUEST,
                    null, "X01004", request);
        }
        //cek verifikasi atau aktivasi akun
        //user db dari database
        User userDB = optUser.get();
        if (!userDB.getIsRegistered()) {
            return new ResponseHandler().handleResponse("USER TIDAK TERDAFTAR",
                    HttpStatus.BAD_REQUEST,
                    null, "X01005", request);
        }
        //proses pencocokan password menggunakan hash
        String password = userDB.getPassword();// yang sudah di hash dari table
        if (!BcryptImpl.verifyHash((user.getUsername() + user.getPassword()), password)) {
            return new ResponseHandler().handleResponse("USER NAME ATAU PASSWORD SALAH",
                    HttpStatus.BAD_REQUEST,
                    null, "X01006", request);
        }
//        ngeset token dan authority nya/permission
        UserDetails userDetails = loadUserByUsername(user.getUsername()); //jwt butuhnya user details
        Map<String, Object> mapForJwt = new HashMap<>();
//        mapForJwt.put("un", userDB.getUsername());
//        mapForJwt.put("pwd", userDB.getPassword());
        //dari token jwt
        mapForJwt.put("uid", userDB.getId());
        mapForJwt.put("ml", userDB.getEmail());
        mapForJwt.put("fn", userDB.getFirstName());
        mapForJwt.put("ln",userDB.getLastName());
        mapForJwt.put("pn", userDB.getNoHp());

        String token = jwtUtility.generateToken(userDetails, mapForJwt);
        Map<String, Object> mapResponse = new HashMap<>();// map untuk response
        mapResponse.put("token", Crypto.performEncrypt(token));//kalau mau di encrypt
        List<RespMenuDTO> ltMenu = modelMapper.map(userDB.getRole().getLtMenu(), new TypeToken<List<RespMenuDTO>>() {
        }.getType());
        mapResponse.put("menu",ltMenu);
//        mapResponse.put("token",token);//kalau mau di encrypt
        //mapResponse.put("menu", new TransformationData().doTransformAksesMenuLogin(ltMenu));
        return ResponseEntity.status(HttpStatus.OK).body(mapResponse);
    }

    @Transactional
    public ResponseEntity<Object> regis(User user, HttpServletRequest request) throws UsernameNotFoundException {

        /** default untuk proses registrasi, user akan mendapatkan role dengan ID 2 */
        Role role = new Role();
        role.setId(2L);

        Optional<User> optUser = userRepo.findByUsername(user.getUsername());
        User userDB = null;
        Integer otp = 0;

        if (optUser.isPresent()) {
            userDB = optUser.get();
            if(userDB.getIsRegistered()){
                return new ResponseHandler().handleResponse("USER SUDAH TERDAFTAR",
                        HttpStatus.BAD_REQUEST,
                        null,"X01007",request);
            }else {
                /** pengecekan seluruh data selain username , jika email atau pun no hp yang ada dan pernah teregistrasi ataupun tidak maka
                 *  user diminta untuk mengganti data tersebut
                 */
                List<User> ltUser = userRepo.findByUsernameOrNoHpOrEmailAndIsRegistered(user.getUsername(),user.getNoHp(),user.getEmail(),true);
                User userCheck = ltUser.get(0);
                if(!ltUser.isEmpty()){
                    /** email sudah ada di database */
                    if(userCheck.getEmail().equals(user.getEmail())){
                        return new ResponseHandler().handleResponse("EMAIL SUDAH TERPAKAI",
                                HttpStatus.BAD_REQUEST,
                                null,"X01008",request);
                    }
                    /** no hp sudah ada di database */
                    if(userCheck.getNoHp().equals(user.getNoHp())){
                        return new ResponseHandler().handleResponse("NO-HP SUDAH TERPAKAI",
                                HttpStatus.BAD_REQUEST,
                                null,"X01008",request);
                    }
                }else{
                    /** PERNAH REGISTRASI TAPI BELUM SELESAI */
                    userDB.setAddress(user.getAddress());
                    userDB.setNoHp(user.getNoHp());
                    userDB.setEmail(user.getEmail());
                    userDB.setFirstName(user.getFirstName());
                    userDB.setLastName(user.getLastName());
                    userDB.setNoHp(user.getNoHp());
                    userDB.setPassword(BcryptImpl.hash(user.getUsername()+user.getPassword()));
                    userDB.setTanggalLahir(user.getTanggalLahir());
                    userDB.setUpdatedBy(userDB.getFirstName()+userDB.getLastName());
                    userDB.setUpdatedDate(new Date());
                    otp = random.nextInt(111111,999999);
                    userDB.setOtp(BcryptImpl.hash(String.valueOf(otp)));
                    userDB.setRole(role);
                }
            }
        }else {
            user.setCreatedBy("Paul");
            user.setCreatedDate(new Date());
            user.setPassword(BcryptImpl.hash(user.getUsername()+user.getPassword()));
            otp = random.nextInt(111111,999999);
            user.setOtp(BcryptImpl.hash(String.valueOf(otp)));
            user.setRole(role);
            userRepo.save(user);
        }
        /** kirim verifikasi email */
        Map<String,Object> mapResponse = new HashMap<>();
        if(OtherConfig.getEnableAutomation().equals("y")){
            mapResponse.put("token", otp);
        }
        /** kalau mau send email, lihat di class ContohController API kirim-email  */
        SendMailOTP.verifyRegisOTP("OTP Registrasi",user.getFirstName(),user.getLastName(),user.getEmail(),String.valueOf(otp));
        System.out.println(otp);
//        mapResponse.put("estafet",);//untuk security estafet work flow pada form
        return ResponseEntity.status(HttpStatus.OK).body(mapResponse);
    }

    @Transactional
    public ResponseEntity<Object> verifyRegis(User user, HttpServletRequest request) throws UsernameNotFoundException {
        if(user==null){
            return new ResponseHandler().handleResponse("DATA TIDAK VALID",
                    HttpStatus.BAD_REQUEST,
                    null,"X01009",request);
        }
        Optional<User> optionalUser = userRepo.findByEmail(user.getEmail());
        if (!optionalUser.isPresent()) {
            return new ResponseHandler().handleResponse("DATA TIDAK VALID",
                    HttpStatus.BAD_REQUEST,
                    null,"X01008",request);
        }
        User userDB = optionalUser.get();
        if(!BcryptImpl.verifyHash(user.getOtp(),userDB.getOtp())){
            return new ResponseHandler().handleResponse("TOKEN SALAH",
                    HttpStatus.BAD_REQUEST,
                    null,"X01009",request);
        }

        userDB.setOtp(BcryptImpl.hash(String.valueOf(random.nextInt(111111,999999))));
        userDB.setIsRegistered(true);
        return new ResponseHandler().handleResponse("Registrasi Berhasil !!",
                HttpStatus.OK,
                null,null,request);
//        return ResponseEntity.status(HttpStatus.OK).body("Registrasi Berhasil !!");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User>  opUser = userRepo.findByUsername(username);
        if(!opUser.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        User user = opUser.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),user.getAuthorities());
    }

    public User convertToUser(ValLoginDTO valLoginDTO) {
        return modelMapper.map(valLoginDTO,User.class);
    }

    public User convertToUser(ValRegisDTO valRegisDTO) {
        return modelMapper.map(valRegisDTO,User.class);
    }

    public User convertToUser(ValVerifyRegisDTO valVerifyRegisDTO) {
        return modelMapper.map(valVerifyRegisDTO,User.class);
    }

}