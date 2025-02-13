package com.juaracoding.tugasakhir.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.core.IService;
import com.juaracoding.tugasakhir.dto.respone.RespHotelDTO;
import com.juaracoding.tugasakhir.dto.validasi.HotelRegistrationDTO;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Hotel;
import com.juaracoding.tugasakhir.model.HotelImage;
import com.juaracoding.tugasakhir.model.Room;
import com.juaracoding.tugasakhir.model.User;
import com.juaracoding.tugasakhir.repository.*;
import com.juaracoding.tugasakhir.service.HotelService;
import com.juaracoding.tugasakhir.util.LoggingFile;
import com.juaracoding.tugasakhir.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService<Hotel> {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RoomRepository  roomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelImageRepository hotelImageRepository;
    @Autowired
    private RoomServiceImpl roomServiceImpl;

    @Autowired
    private Cloudinary cloudinary;

    public static final String BASE_URL_IMAGE = System.getProperty("user.dir")+"\\image-saved";
    private static Path rootPath;

    @Autowired
    private ModelMapper modelMapper;

    private AddressServiceImpl addressServiceImpl;

    @Autowired
    private TransformPagination transformPagination;

    @Override
    public ResponseEntity<Object> save(Hotel hotel, List<MultipartFile> hotelImages, HttpServletRequest request) {
//        Map map;
//        rootPath = Paths.get(BASE_URL_IMAGE+"/"+new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date()));
//        String strPathz = rootPath.toAbsolutePath().toString();

        try {
            Optional<Hotel> hotelExisting = hotelRepository.findByName(hotel.getName());
            if (hotelExisting.isPresent()) {
                return new ResponseHandler().handleResponse("Nama Hotel Sudah Terdaftar",
                        HttpStatus.BAD_REQUEST,null,"FVAUT01001", request);
            }

//            addressRepository.save(hotel.getAddress());

            // Set bidirectional relationship for rooms
            for (Room room : hotel.getRooms()) {
                room.setHotel(hotel);
            }
//            String strPathzImage = strPathz+"\\"+file.getOriginalFilename();
//            saveFile(file);
//            map = cloudinary.uploader().upload(strPathzImage, ObjectUtils.asMap("public_id",file.getOriginalFilename()));
//            hotel.setPathImage(strPathzImage);
//            hotel.setLinkImage(map.get("secure_url").toString());
//            hotelRepository.save(hotel);
            // **1. Simpan hotel terlebih dahulu agar memiliki ID**
            hotel = hotelRepository.save(hotel);

            List<HotelImage> images = new ArrayList<>();

            // **2. Simpan gambar setelah hotel memiliki ID**
            for (MultipartFile file : hotelImages) {
                String imageUrl = uploadFile(file);
                HotelImage hotelImage = new HotelImage();
                hotelImage.setLinkImage(imageUrl);
                hotelImage.setHotel(hotel); // Sekarang hotel sudah memiliki ID
                images.add(hotelImageRepository.save(hotelImage));
            }

            // **3. Set daftar gambar ke hotel dan update hotel**
            hotel.setHotelImages(images);
            hotelRepository.save(hotel); // Simpan ulang hotel agar daftar gambar tersimpan

        }catch (Exception e){
            LoggingFile.logException("HotelService", "save",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Disimpan",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT01001", request);
        }

        return new ResponseHandler().handleResponse("Data Berhasil Disimpan",
                HttpStatus.CREATED,null,null, null);
    }
    @Transactional
    @Override
    public ResponseEntity<Object> update(Long id, Hotel hotel, HttpServletRequest request) {
        try{
            Optional<Hotel> existingHotel = hotelRepository.findById(id);
            if (!existingHotel.isPresent()) {
                return new ResponseHandler().handleResponse("Hotel Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST,null,"FVAUT01002", request);
            }
            Hotel hotelDB = existingHotel.get();
            hotelDB.setName(hotel.getName());

            hotelDB.getAddress().setStreetName(hotel.getAddress().getStreetName());
            hotelDB.getAddress().setCity(hotel.getAddress().getCity());
            hotelDB.getAddress().setCountry(hotel.getAddress().getCountry());

            for (Room room : hotel.getRooms()) {
              roomServiceImpl.saveRoom(room, hotelDB);
            }
           hotelDB.setDescription(hotel.getDescription());

            hotelRepository.save(hotelDB);
        }catch (Exception e){
            LoggingFile.logException("HotelService", "update",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Diupdate",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT01002", request);
        }
        return new ResponseHandler().handleResponse("Data Berhasil Diubah",
                HttpStatus.OK,null,null, null);
    }
    @Transactional
    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        try {
            Optional<Hotel> existingHotel = hotelRepository.findById(id);
            if (!existingHotel.isPresent()) {
                return new ResponseHandler().handleResponse("Hotel Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST,null,"FVAUT01003", request);
            }
            hotelRepository.delete(existingHotel.get());
        }catch (Exception e){
            LoggingFile.logException("HotelService", "delete",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Dihapus",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT01003", request);
        }
        return new ResponseHandler().handleResponse("Data Berhasil Dihapus",
                HttpStatus.OK,null,null, null);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Map<String, Object> mapList;
        try{
            Page<Hotel> page = null;
            List<Hotel> list = null;
            page = hotelRepository.findAll(pageable);
            list = page.getContent();
            List<RespHotelDTO> listDTO = convertToListRespHotelDTO(list);

            if (list.isEmpty()){
                return new ResponseHandler().handleResponse("Hotel Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST,null,"FVAUT01004", request);
            }
            mapList = transformPagination.transformPagination(listDTO,page,"id", "");
        } catch (Exception e) {
            LoggingFile.logException("HotelService", "findAll",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Diakses",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT01003", request);
        }
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.OK,
                mapList,null,request);
    }

    public ResponseEntity<Object> findAllByManagerId(Pageable pageable,Long id, HttpServletRequest request) {
        Map<String, Object> mapList;
        try {
            Page<Hotel> page = null;
            List<Hotel> list = null;
            page = hotelRepository.findAllByUser_Id(pageable, id);
            list = page.getContent();
            List<RespHotelDTO> listDTO = convertToListRespHotelDTO(list);
            Optional<User> user = userRepository.findById(id);

            if (list.isEmpty() && user.isPresent()) {
                return new ResponseHandler().handleResponse("Hotel Masih Kosong",
                        HttpStatus.OK,null,null, request);
            }
            if(!user.isPresent()){
                return new ResponseHandler().handleResponse("Data Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST,null,"FVAUT01005", request);
            }
            mapList = transformPagination.transformPagination(listDTO,page,"id", "");
        } catch (Exception e) {
            LoggingFile.logException("HotelService", "findAllByManagerId",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Diakses",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT01003", request);
        }
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.OK,
                mapList,null,request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        RespHotelDTO respHotelDTO;
        try{
            Optional<Hotel> existingHotel = hotelRepository.findById(id);
            if (!existingHotel.isPresent()) {
                return new ResponseHandler().handleResponse("Hotel Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST,null,"FVAUT01003", request);
            }
            Hotel hotelDB = existingHotel.get();
            respHotelDTO = modelMapper.map(hotelDB, RespHotelDTO.class);
        } catch (Exception e) {
            LoggingFile.logException("HotelService", "findById",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Diakses",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT01003", request);
        }
        return new ResponseHandler().handleResponse("OK", HttpStatus.OK,respHotelDTO,null,request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        return null;
    }

    public Hotel mapHotelRegistrationDTOtoHotel(HotelRegistrationDTO hotelRegistrationDTO) {
        return modelMapper.map(hotelRegistrationDTO, Hotel.class);
    }
    public List<RespHotelDTO> convertToListRespHotelDTO(List<Hotel> respHotelList){
        return modelMapper.map(respHotelList,new TypeToken<List<RespHotelDTO>>(){}.getType());
    }

    public void saveFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Gagal Untuk menyimpan File kosong !!");
            }
            Path destinationFile = this.rootPath.resolve(Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootPath.toAbsolutePath())) {
                // This is a security check
                throw new IllegalArgumentException(
                        "Tidak Dapat menyimpan file diluar storage yang sudah ditetapkan !!");
            }
            Files.createDirectories(this.rootPath);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Failed to store file.", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString(); // Ambil URL gambar yang di-upload
        } catch (IOException e) {
            throw new RuntimeException("Gagal mengunggah gambar ke Cloudinary", e);
        }
    }
}
