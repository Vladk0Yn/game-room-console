package com.yanovych.services.implementations;

import com.yanovych.entities.Child;
import com.yanovych.entities.Room;
import com.yanovych.entities.Toy;
import com.yanovych.repository.implementations.ToyFromFileRepository;
import com.yanovych.repository.interfaces.ToyRepository;
import com.yanovych.services.interfaces.ToyService;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ToyServiceImplementation implements ToyService {
    private static ToyServiceImplementation instance = null;
    private final ToyRepository toyRepository = ToyFromFileRepository.getInstance();
    private ToyServiceImplementation() {
    }

    public static ToyServiceImplementation getInstance() {
        if (instance == null) {
            instance = new ToyServiceImplementation();
        }
        return instance;
    }

    @Override
    public Toy getToyById(Long id) {
        Toy toy = this.toyRepository.getToyById(id);
        if (toy == null) {
            log.error("IN getToyById - no toy with id: {}", id);
            return null;
        }
        log.info("IN getToyById - toy: {} successfully found", toy.getName());
        return toy;
    }

    @Override
    public void createToy(Toy toy) {
        toyRepository.addToy(toy);
        log.info("IN createToy - toy: {} successfully created", toy.getName());
    }

    @Override
    public List<Toy> getAllToys() {
        List<Toy> toys = toyRepository.getAllToys();
        log.info("IN getAllToys - toys: {} successfully received", toys.size());
        return toys;
    }

    @Override
    public List<Toy> getToysWithoutRoom() {
        List<Toy> toys = getAllToys().stream().filter(c -> c.getToyRoomId() == null).toList();
        log.info("IN getToysWithoutRoom - {} toys successfully found", toys.size());
        return toys;
    }

    @Override
    public List<Toy> sortToysInRoomByType(Room room) {
        return room.getToysInRoom().stream()
                .sorted(Comparator.comparing(Toy::getType))
                .toList();
    }

    @Override
    public List<Toy> findToysInRoomByDiapasonOfPrice(Room room, Double priceMin, Double priceMax) {
        return room.getToysInRoom().stream()
                .filter(toy -> toy.getPrice() >= priceMin && toy.getPrice() <= priceMax)
                .toList();
    }
}
