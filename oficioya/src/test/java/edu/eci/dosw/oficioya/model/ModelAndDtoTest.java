package edu.eci.dosw.oficioya.model;

import edu.eci.dosw.oficioya.dto.*;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static org.junit.jupiter.api.Assertions.*;

class ModelAndDtoTest {

    @Test
    void testUserAndWorkerModels() throws MalformedURLException {
        URL url = URI.create("http://example.com/foto.jpg").toURL();
        User u = new User(10, "Test User", "test@user.com", 300111223, "pass", url);
        assertEquals(10, u.getId());
        assertEquals("Test User", u.getNombre());
        assertEquals("test@user.com", u.getCorreo());
        assertEquals(300111223, u.getTelefono());
        assertEquals("pass", u.getPasswd());
        assertEquals(url, u.getFoto());

        u.setId(11);
        u.setNombre("Updated");
        u.setCorreo("new@user.com");
        u.setTelefono(300999888);
        u.setPasswd("newpass");
        u.setFoto(null);
        u.setNotifications(new ArrayList<>());
        assertNotNull(u.getNotifications());

        Worker w = new Worker();
        w.setId(1);
        w.setUser(u);
        u.setWorker(w);
        assertEquals(w, u.getWorker());
        assertEquals(u, w.getUser());

        // Probar métodos delegados de composición
        assertEquals("Updated", w.getNombre());
        assertEquals("new@user.com", w.getCorreo());
        assertEquals(300999888, w.getTelefono());
        assertEquals("newpass", w.getPasswd());
        assertNull(w.getFoto());

        w.setNombre("WorkerNombre");
        w.setCorreo("work@mail.com");
        w.setTelefono(310111222);
        w.setPasswd("secret123");
        w.setFoto(url);
        assertEquals("WorkerNombre", u.getNombre());
        assertEquals("work@mail.com", u.getCorreo());
        assertEquals(310111222, u.getTelefono());
        assertEquals("secret123", u.getPasswd());
        assertEquals(url, u.getFoto());

        w.setSalary(1000000);
        w.setFinishedJobs(10);
        w.setFinishJobsPictures(new ArrayList<>());
        w.setWorkRequests(new ArrayList<>());
        w.setSecondaryJobs(new ArrayList<>());
        w.setDisponibilities(new ArrayList<>());
        w.setWorkZone(new WorkZone("Chapinero"));

        assertEquals(1000000, w.getSalary());
        assertEquals(10, w.getFinishedJobs());
        assertNotNull(w.getFinishJobsPictures());
        assertNotNull(w.getWorkRequests());
        assertNotNull(w.getSecondaryJobs());
        assertNotNull(w.getDisponibilities());
        assertNotNull(w.getWorkZone());

        Worker wSimple = new Worker(2, "Simple", "simple@mail.com");
        assertEquals(2, wSimple.getId());
        assertEquals("Simple", wSimple.getNombre());
        assertEquals("simple@mail.com", wSimple.getCorreo());

        Administrator admin = new Administrator();
        assertNotNull(admin);
    }

    @Test
    void testJobAndWorkZoneAndDisponibility() {
        Job job = new Job();
        job.setDescription("Carpinteria");
        job.setMaterialsRequired(true);
        assertEquals("Carpinteria", job.getDescription());
        assertTrue(job.isMaterialsRequired());

        WorkZone zone = new WorkZone();
        zone.setId(1);
        zone.setCiudad("Bogota");
        zone.setLocalidad("Usaquen");
        zone.setBarrio("Santa Barbara");
        assertEquals(1, zone.getId());
        assertEquals("Bogota", zone.getCiudad());
        assertEquals("Usaquen", zone.getLocalidad());
        assertEquals("Santa Barbara", zone.getBarrio());

        Disponibility disp = new Disponibility();
        disp.setActive(true);
        LocalDateTime now = LocalDateTime.now();
        disp.setDate(now);
        Timer t1 = new Timer();
        Timer t2 = new Timer();
        disp.setStartHour(t1);
        disp.setFinishHour(t2);

        assertTrue(disp.isActive());
        assertEquals(now, disp.getDate());
        assertEquals(t1, disp.getStartHour());
        assertEquals(t2, disp.getFinishHour());
        t1.cancel();
        t2.cancel();

        Disponibility disp2 = new Disponibility(true, now, null, null);
        assertNotNull(disp2);
    }

    @Test
    void testDtos() throws MalformedURLException {
        URL url = URI.create("http://example.com/foto.jpg").toURL();
        WorkerRegistrationDTO reg = new WorkerRegistrationDTO();
        reg.setNombre("Name");
        reg.setCorreo("mail@test.com");
        reg.setTelefono(123456);
        reg.setPasswd("secret");
        reg.setFoto(url);
        reg.setRate(25000);
        reg.setSalary(500000);
        reg.setWorkZone(new WorkZone("Suba"));
        reg.setSecondaryJobs(new ArrayList<>());
        reg.setDisponibilities(new ArrayList<>());
        reg.setPrincipalJob(new Job("Electricista"));

        assertEquals("Name", reg.getNombre());
        assertEquals("mail@test.com", reg.getCorreo());
        assertEquals(123456, reg.getTelefono());
        assertEquals("secret", reg.getPasswd());
        assertEquals(url, reg.getFoto());
        assertEquals(25000, reg.getRate());
        assertEquals(500000, reg.getSalary());
        assertNotNull(reg.getWorkZone());
        assertNotNull(reg.getSecondaryJobs());
        assertNotNull(reg.getDisponibilities());
        assertNotNull(reg.getPrincipalJob());

        WorkerResponseDTO res = new WorkerResponseDTO();
        res.setId(5);
        res.setNombre("Res");
        res.setFoto(url);
        res.setRate(30000);
        res.setSalary(700000);
        res.setFinishedJobs(4);
        res.setState(true);
        res.setPrincipalJob(new Job("Pintor"));
        res.setSecondaryJobs(new ArrayList<>());
        res.setWorkZone(new WorkZone("Usaquen"));
        res.setDisponibilities(new ArrayList<>());

        assertEquals(5, res.getId());
        assertEquals("Res", res.getNombre());
        assertEquals(url, res.getFoto());
        assertEquals(30000, res.getRate());
        assertEquals(700000, res.getSalary());
        assertEquals(4, res.getFinishedJobs());
        assertTrue(res.isState());
        assertNotNull(res.getPrincipalJob());
        assertNotNull(res.getSecondaryJobs());
        assertNotNull(res.getWorkZone());
        assertNotNull(res.getDisponibilities());

        WorkerUpdateDTO upd = new WorkerUpdateDTO();
        upd.setRate(40000);
        upd.setSalary(800000);
        upd.setPrincipalJob(new Job("Plomero"));
        upd.setSecondaryJobs(new ArrayList<>());
        upd.setWorkZone(new WorkZone("Chapinero"));
        upd.setDisponibilities(new ArrayList<>());
        upd.setNombre("Upd");
        upd.setFoto(url);

        assertEquals(40000, upd.getRate());
        assertEquals(800000, upd.getSalary());
        assertNotNull(upd.getPrincipalJob());
        assertNotNull(upd.getSecondaryJobs());
        assertNotNull(upd.getWorkZone());
        assertNotNull(upd.getDisponibilities());
        assertEquals("Upd", upd.getNombre());
        assertEquals(url, upd.getFoto());

        LoginResponseDTO loginRes = new LoginResponseDTO();
        loginRes.setAuthenticated(true);
        loginRes.setMessage("OK");
        loginRes.setUserId(1);
        loginRes.setNombre("User");
        loginRes.setCorreo("user@mail.com");

        assertTrue(loginRes.isAuthenticated());
        assertEquals("OK", loginRes.getMessage());
        assertEquals(1, loginRes.getUserId());
        assertEquals("User", loginRes.getNombre());
        assertEquals("user@mail.com", loginRes.getCorreo());
    }
}
