package org.example.mapper;

import org.example.dto.RequestExcelDTO;
import org.example.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Named("getEmail")
    default String getEmail(Request request) {
        if (request.getUser() != null) {
            return request.getUser().getEmail();
        } else {
            return null;
        }
    }

    @Mapping(target = "email", expression = "java(getEmail(request))")
    RequestExcelDTO requestToRequestExcel(Request request);

    List<RequestExcelDTO> requestToRequestExcel(List<Request> list);

}
