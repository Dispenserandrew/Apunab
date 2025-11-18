package ui;

import javax.swing.*;
import java.awt.*;
import service.UsuarioService;
import model.Usuario;

public class RegistroUI extends JFrame {
    private UsuarioService usuarioService = new UsuarioService();

    public RegistroUI() {
        setTitle("APUNAB - Registro");
        setSize(480, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel title = new JLabel("Crear cuenta APUNAB", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(60, 20, 360, 30);
        add(title);

        JLabel lNombre = new JLabel("Nombre completo:");
        lNombre.setBounds(60, 80, 360, 20);
        add(lNombre);
        JTextField txtNombre = new JTextField(); txtNombre.setBounds(60,105,360,34); add(txtNombre);

        JLabel lCodigo = new JLabel("Código (opcional):");
        lCodigo.setBounds(60,150,360,20);
        add(lCodigo);
        JTextField txtCodigo = new JTextField(); txtCodigo.setBounds(60,175,360,34); add(txtCodigo);

        JLabel lEmail = new JLabel("Correo institucional:");
        lEmail.setBounds(60,220,360,20);
        add(lEmail);
        JTextField txtEmail = new JTextField(); txtEmail.setBounds(60,245,360,34); add(txtEmail);

        JLabel lPass = new JLabel("Contraseña:");
        lPass.setBounds(60,290,360,20);
        add(lPass);
        JPasswordField txtPass = new JPasswordField(); txtPass.setBounds(60,315,360,34); add(txtPass);

        JButton btnCrear = new JButton("Crear cuenta");
        btnCrear.setBounds(60,380,360,40);
        btnCrear.setBackground(new Color(84,167,255)); btnCrear.setForeground(Color.white);
        btnCrear.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String codigo = txtCodigo.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword());
            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos obligatorios");
                return;
            }
            if (codigo.isEmpty()) codigo = String.valueOf(100000 + new java.util.Random().nextInt(900000));
            Usuario u = new Usuario(codigo, nombre, email, pass, 2000);
            usuarioService.registrar(u);
            JOptionPane.showMessageDialog(this, "Registro correcto. Ya puedes ingresar.");
            new LoginUI().setVisible(true);
            dispose();
        });
        add(btnCrear);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(60, 430, 360, 34);
        btnVolver.addActionListener(e -> { new LoginUI().setVisible(true); dispose(); });
        add(btnVolver);
    }
}
