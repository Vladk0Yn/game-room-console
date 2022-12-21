package com.yanovych.repository.implementations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanovych.entities.Child;
import com.yanovych.helpers.ObjectFileReader;
import com.yanovych.helpers.ObjectFileWriter;
import com.yanovych.repository.interfaces.ChildRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChildFromFileRepository implements ChildRepository {

    private static ChildFromFileRepository instance = null;

    private ChildFromFileRepository() {
    }

    public static ChildFromFileRepository getInstance() {
        if (instance == null) {
            instance = new ChildFromFileRepository();
        }
        return instance;
    }

    @Override
    public Child getChildById(long id) {
        List<Child> children =  this.getAllChildren();
        return children.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Child> getAllChildren() {
        String childrenListJson = ObjectFileReader.read("children.json");
        return new Gson().fromJson(childrenListJson, new TypeToken<List<Child>>(){}.getType());
    }

    @Override
    public void addChild(Child child) {
        List<Child> children = this.getAllChildren();
        if (children.isEmpty()) {
            children = new ArrayList<>();
            child.setId(1L);
        } else {
            Long lastChildId = children.stream().max(Comparator.comparingLong(Child::getId)).get().getId();
            child.setId(++lastChildId);
        }
        children.add(child);
        String childrenJsonFormat = new Gson().toJson(children);
        ObjectFileWriter.write("children.json", childrenJsonFormat, false);
    }
}
