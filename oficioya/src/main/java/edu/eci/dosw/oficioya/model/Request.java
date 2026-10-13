package edu.eci.dosw.oficioya.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Request {
    private String Job;
    private String description;
    private ArrayList<URL> pictures = new ArrayList<>();
    private String state;
    private Timer timer;
    private String address;

    private UserModel employer;
    private WorkerModel worker;
    private List<Disponibility> workTime = new ArrayList<>();
}
