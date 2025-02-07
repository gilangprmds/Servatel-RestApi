package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.dto.response.RespHotelDTO;
import com.juaracoding.tugasakhir.dto.validasi.HotelRegistrationDTO;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Hotel;
import com.juaracoding.tugasakhir.model.Room;
import com.juaracoding.tugasakhir.repository.AddressRepository;
import com.juaracoding.tugasakhir.repository.HotelRepository;
import com.juaracoding.tugasakhir.repository.RoomRepository;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService<Hotel> {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RoomRepository  roomRepository;

    @Autowired
    private RoomServiceImpl roomServiceImpl;

    @Autowired
    private ModelMapper modelMapper;

    private AddressServiceImpl addressServiceImpl;

    @Autowired
    private TransformPagination transformPagination;

    @Override
    public ResponseEntity<Object> save(Hotel hotel, HttpServletRequest request) {
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
            hotelRepository.save(hotel);

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

            if (list.isEmpty()){
                return new ResponseHandler().handleResponse("Hotel Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST,null,"FVAUT01004", request);
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
}
