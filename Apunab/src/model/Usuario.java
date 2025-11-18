package model;

public class Usuario {
    private String codigo;
    private String nombre;
    private String email;
    private String password;
    private int apunab;

    public Usuario(String codigo, String nombre, String email, String password, int apunab) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.apunab = apunab;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getApunab() { return apunab; }
    public void setApunab(int apunab) { this.apunab = apunab; }
}
