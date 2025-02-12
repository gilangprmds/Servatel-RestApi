package com.juaracoding.tugasakhir.util;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 09/02/2025 13:46
@Last Modified 09/02/2025 13:46
Version 1.0
*/
//import org.modelmapper.ModelMapper;
//import org.modelmapper.PropertyMap;
//import org.modelmapper.TypeToken;
//
//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//public class TransformationData {
//    private int intListMenuSize = 0;
//    private final List<Object> lObj = new ArrayList<>();
//    private ModelMapper modelMapper;
//
//
//    public List<Object> doTransformAksesMenuLogin(List<MenuLoginDTO> listMenu){
//        lObj.clear();
//        intListMenuSize = listMenu.size();
//        /**
//         * Grouping berdasarkan field getNamaGroupMenu
//         */
//        List<MenuMappingDTO> z = modelMapper.map(listMenu, new TypeToken<List<MenuMappingDTO>>() {}.getType());
//        Map<String, List<MenuMappingDTO>> map = groupBy(z, MenuMappingDTO::getName);
//        Map<String ,Object> map2 = null;
//        for (Map.Entry<String,List<MenuMappingDTO>> x:
//                map.entrySet()) {
//            map2 = new HashMap<>();
//            map2.put("menu",x.getValue());
//            lObj.add(map2);
//        }
//        return lObj;
//    }
//
//    public <E, K> Map<K, List<E>> groupBy(List<E> list, Function<E, K> keyFunction) {
//        return Optional.ofNullable(list)
//                .orElseGet(ArrayList::new)
//                .stream().skip(0)
//                .collect(Collectors.groupingBy(keyFunction,Collectors.toList()));
//    }
//}