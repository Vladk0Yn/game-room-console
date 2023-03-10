package com.yanovych.repository.implementations;

import com.google.gson.reflect.TypeToken;
import com.yanovych.entities.Child;
import com.yanovych.entities.Room;
import com.yanovych.helpers.ObjectFileReader;
import com.yanovych.helpers.ObjectFileWriter;
import com.yanovych.repository.interfaces.ChildRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ChildFromFileRepository implements ChildRepository {
    private final ObjectFileReader<Child> reader;
    private final ObjectFileWriter<Child> writer;
    private List<Child> children;
    private static ChildFromFileRepository instance = null;

    private ChildFromFileRepository() {
        this.reader = new ObjectFileReader<>();
        this.writer = new ObjectFileWriter<>();
        this.children = this.getAllChildren();
    }

    public static ChildFromFileRepository getInstance() {
        if (instance == null) {
            instance = new ChildFromFileRepository();
        }
        return instance;
    }

    @Override
    public Child getChildById(Long id) {
        return getAllChildren().stream()
                .filter(child -> Objects.equals(child.getId(), id)).findAny().orElse(null);
    }

    @Override
    public List<Child> getAllChildren() {
        return this.reader.readListOfObjects("children.json", new TypeToken<ArrayList<Child>>(){}.getType());
    }

    @Override
    public void addChild(Child child) {
        if (this.children == null || this.children.isEmpty()) {
            this.children = new ArrayList<>();
            child.setId(1L);
        } else {
            Long lastChildId = this.children.stream()
                    .max(Comparator.comparingLong(Child::getId)).get().getId();
            child.setId(++lastChildId);
        }
        this.children.add(child);
        this.writer.writeListOfObjects("children.json", this.children, false);
    }

    @Override
    public void updateChild(Child child) {
        for (int i = 0; i < this.children.size(); i++) {
            if (child.getId().equals(this.children.get(i).getId())) {
                this.children.set(i, child);
                this.writer.writeListOfObjects("children.json", this.children, false);
                break;
            }
        }
    }

    @Override
    public void addChildToRoom(Child child, Room room) {
        child.setRoomId(room.getId());
        this.updateChild(child);
    }
}
