package org.example.sendov.controller;

import org.example.sendov.model.Model;

public class Controller {
    private final Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public void updatePoly(String poly) {model.updatePoly(poly);}

    public void init() {

    }
}
