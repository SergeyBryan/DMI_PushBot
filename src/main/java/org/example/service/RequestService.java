package org.example.service;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import org.example.dto.RequestExcelDTO;
import org.example.entity.Request;
import org.example.mapper.RequestMapperImpl;
import org.example.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapperImpl requestMapper;


    public RequestService(RequestRepository requestRepository, RequestMapperImpl requestMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }

    public void create(Request request) {
        requestRepository.save(request);
    }

    public List<Request> requestList() {
        return requestRepository.findAll();
    }


    public File requestsToExcel() throws IOException {
        List<Request> requests = requestList();
        List<RequestExcelDTO> requestExcels = requestMapper.requestToRequestExcel(requests);
        return createFile(requestExcels);
    }

    public File createFile(List<RequestExcelDTO> list) throws IOException {
        File file = new File("request.xlsx");
        Xcelite xcelite = new Xcelite();
        try {
            file.createNewFile();
            XceliteSheet xceliteSheet = xcelite.createSheet("requests");
            SheetWriter<RequestExcelDTO> writer = xceliteSheet.getBeanWriter(RequestExcelDTO.class);
            writer.write(list);
            xcelite.write(file);
            return file;
        } catch (IOException e) {
            throw new IOException("Failed to create file");
        }
    }

    @Transactional
    public void deleteRequestsByWeek() {
        LocalDateTime localDateTime = LocalDateTime.now().minusWeeks(1);
        requestRepository.deleteRequestsByWeek(localDateTime);
    }

}