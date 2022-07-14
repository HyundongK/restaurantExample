package com.example.restaurant.wishlist.service;

import com.example.restaurant.naver.NaverClient;
import com.example.restaurant.naver.dto.SearchImageReq;
import com.example.restaurant.naver.dto.SearchLocalReq;
import com.example.restaurant.wishlist.dto.WishListDto;
import com.example.restaurant.wishlist.entity.WishListEntity;
import com.example.restaurant.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final NaverClient naverClient;
    private final WishListRepository wishListRepository;

    //지역검색 ->이미지검색 -> 결과물 리턴
    public WishListDto search(String query){
        //지역검색
        var searchLocalReq= new SearchLocalReq();
        searchLocalReq.setQuery(query);

        var searchLocalRes=naverClient.searchLocal(searchLocalReq);

        if(searchLocalRes.getTotal()>0){
            var localItem =searchLocalRes.getItems().stream().findFirst().get();


            //이미지 검색
            var imageQuery= localItem.getTitle().replaceAll("<[^>]*","");
            var searchImageReq=new SearchImageReq();
            searchImageReq.setQuery(imageQuery);

            var searchImageRes=naverClient.searchImage(searchImageReq);

            if(searchImageRes.getTotal()>0){

                var imageItem=searchImageRes.getItems().stream().findFirst().get();

                var result=new WishListDto();
                result.setTitle(localItem.getTitle());
                result.setCategory(localItem.getCategory());
                result.setAddress(localItem.getAddress());
                result.setRoadAddress(localItem.getRoadAddress());
                result.setHomePageLink(localItem.getLink());
                result.setImageLink(imageItem.getLink());

                return result;
            }
        }
        return new WishListDto();

    }

    public WishListDto add(WishListDto wishListDto) {
     var entity=dtoToEntity(wishListDto);
     var saveEntity=wishListRepository.save(entity);
     return entityToDto(saveEntity);
    }

    private WishListEntity dtoToEntity(WishListDto wishListDto){
        var entity=new WishListEntity();
        entity.setIndex(wishListDto.getIndex());
        entity.setImageLink(wishListDto.getImageLink());
        entity.setTitle(wishListDto.getTitle());
        entity.setCategory(wishListDto.getCategory());
        entity.setVisit(wishListDto.isVisit());
        entity.setVisitCount(wishListDto.getVisitCount());
        entity.setRoadAddress(wishListDto.getRoadAddress());
        entity.setAddress(wishListDto.getAddress());
        entity.setHomePageLink(wishListDto.getHomePageLink());
        entity.setLastVisitDate(wishListDto.getLastVisitDate());
        return entity;
    }
    private WishListDto entityToDto(WishListEntity wishListEntity){
        var dto=new WishListDto();
        dto.setIndex(wishListEntity.getIndex());
        dto.setImageLink(wishListEntity.getImageLink());
        dto.setTitle(wishListEntity.getTitle());
        dto.setCategory(wishListEntity.getCategory());
        dto.setVisit(wishListEntity.isVisit());
        dto.setVisitCount(wishListEntity.getVisitCount());
        dto.setRoadAddress(wishListEntity.getRoadAddress());
        dto.setAddress(wishListEntity.getAddress());
        dto.setHomePageLink(wishListEntity.getHomePageLink());
        dto.setLastVisitDate(wishListEntity.getLastVisitDate());
        return dto;
    }

    public List<WishListDto> findAll(){
        return wishListRepository.findAll()
                .stream()
                .map(it->entityToDto(it))
                .collect(Collectors.toList());
    }

    public void delete(int index){
        wishListRepository.deleteById(index);
    }

    public void addVisit(int index){
        var wishItem=wishListRepository.findById(index);
        if(wishItem.isPresent()){
            var item=wishItem.get();
            item.setVisit(true);
            item.setVisitCount(item.getVisitCount()+1);
        }
    }
}
