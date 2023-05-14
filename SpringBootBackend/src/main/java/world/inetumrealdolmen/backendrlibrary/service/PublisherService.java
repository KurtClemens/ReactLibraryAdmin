package world.inetumrealdolmen.backendrlibrary.service;

import world.inetumrealdolmen.backendrlibrary.domain.dao.PublisherDao;
import world.inetumrealdolmen.backendrlibrary.domain.dto.PublisherDto;

import java.util.List;

public interface PublisherService {
    List<PublisherDto> getAllPublishers();

    PublisherDto createPublisher(PublisherDao publisherDao);
}
