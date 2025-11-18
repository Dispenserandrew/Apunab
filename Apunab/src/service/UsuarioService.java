package service;

import model.Usuario;
import java.io.*;
import java.util.*;

public class UsuarioService {
    private File archivo;

    public UsuarioService() {
        archivo = new File("data/usuarios.txt");
        try {
            archivo.getParentFile().mkdirs();
            if (!archivo.exists()) archivo.createNewFile();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean registrar(Usuario u) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(u.getCodigo() + ";" + u.getNombre() + ";" + u.getEmail() + ";" + u.getPassword() + ";" + u.getApunab());
            bw.newLine();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public Usuario login(String idOrEmail, String pass) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                if (d.length < 5) continue;
                String codigo = d[0], nombre = d[1], email = d[2], password = d[3];
                int ap = Integer.parseInt(d[4]);
                if ((codigo.equals(idOrEmail) || email.equals(idOrEmail)) && password.equals(pass)) {
                    return new Usuario(codigo, nombre, email, password, ap);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Usuario> allUsers() {
        List<Usuario> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] d = linea.split(";");
                if (d.length < 5) continue;
                list.add(new Usuario(d[0], d[1], d[2], d[3], Integer.parseInt(d[4])));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void updateAll(List<Usuario> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            for (Usuario u : users) {
                bw.write(u.getCodigo() + ";" + u.getNombre() + ";" + u.getEmail() + ";" + u.getPassword() + ";" + u.getApunab());
                bw.newLine();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
